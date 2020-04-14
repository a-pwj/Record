## 问题：聊天页面TextView 高频度绘图，性能下降

**解决：需要一个静态的文本布局StaticLayout**

1、简单介绍

### TextView的setText方法分析

```java
 public void setText(CharSequence text, BufferType type) {
        setText(text, type, true, 0);

        if (mCharWrapper != null) {
            mCharWrapper.mChars = null;
        }
    }

    private void setText(CharSequence text, BufferType type,
                         boolean notifyBefore, int oldlen) {
        if (text == null) {
            text = "";
        }

        // If suggestions are not enabled, remove the suggestion spans from the text
        if (!isSuggestionsEnabled()) {
            text = removeSuggestionSpans(text);
        }

        if (!mUserSetTextScaleX) mTextPaint.setTextScaleX(1.0f);

        if (text instanceof Spanned &&
            ((Spanned) text).getSpanStart(TextUtils.TruncateAt.MARQUEE) >= 0) {
            if (ViewConfiguration.get(mContext).isFadingMarqueeEnabled()) {
                setHorizontalFadingEdgeEnabled(true);
                mMarqueeFadeMode = MARQUEE_FADE_NORMAL;
            } else {
                setHorizontalFadingEdgeEnabled(false);
                mMarqueeFadeMode = MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS;
            }
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }

        int n = mFilters.length;
        for (int i = 0; i < n; i++) {
            CharSequence out = mFilters[i].filter(text, 0, text.length(), EMPTY_SPANNED, 0, 0);
            if (out != null) {
                text = out;
            }
        }

        if (notifyBefore) {
            if (mText != null) {
                oldlen = mText.length();
                sendBeforeTextChanged(mText, 0, oldlen, text.length());
            } else {
                sendBeforeTextChanged("", 0, 0, text.length());
            }
        }

        boolean needEditableForNotification = false;

        if (mListeners != null && mListeners.size() != 0) {
            needEditableForNotification = true;
        }

        if (type == BufferType.EDITABLE || getKeyListener() != null ||
                needEditableForNotification) {
            createEditorIfNeeded();
            mEditor.forgetUndoRedo();
            Editable t = mEditableFactory.newEditable(text);
            text = t;
            setFilters(t, mFilters);
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null) imm.restartInput(this);
        } else if (type == BufferType.SPANNABLE || mMovement != null) {
            text = mSpannableFactory.newSpannable(text);
        } else if (!(text instanceof CharWrapper)) {
            text = TextUtils.stringOrSpannedString(text);
        }

        if (mAutoLinkMask != 0) {
            Spannable s2;

            if (type == BufferType.EDITABLE || text instanceof Spannable) {
                s2 = (Spannable) text;
            } else {
                s2 = mSpannableFactory.newSpannable(text);
            }

            if (Linkify.addLinks(s2, mAutoLinkMask)) {
                text = s2;
                type = (type == BufferType.EDITABLE) ? BufferType.EDITABLE : BufferType.SPANNABLE;

                /*
                 * We must go ahead and set the text before changing the
                 * movement method, because setMovementMethod() may call
                 * setText() again to try to upgrade the buffer type.
                 */
                mText = text;

                // Do not change the movement method for text that support text selection as it
                // would prevent an arbitrary cursor displacement.
                if (mLinksClickable && !textCanBeSelected()) {
                    setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        }

        mBufferType = type;
        mText = text;

        if (mTransformation == null) {
            mTransformed = text;
        } else {
            mTransformed = mTransformation.getTransformation(text, this);
        }

        final int textLength = text.length();

        if (text instanceof Spannable && !mAllowTransformationLengthChange) {
            Spannable sp = (Spannable) text;

            // Remove any ChangeWatchers that might have come from other TextViews.
            final ChangeWatcher[] watchers = sp.getSpans(0, sp.length(), ChangeWatcher.class);
            final int count = watchers.length;
            for (int i = 0; i < count; i++) {
                sp.removeSpan(watchers[i]);
            }

            if (mChangeWatcher == null) mChangeWatcher = new ChangeWatcher();

            sp.setSpan(mChangeWatcher, 0, textLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE |
                       (CHANGE_WATCHER_PRIORITY << Spanned.SPAN_PRIORITY_SHIFT));

            if (mEditor != null) mEditor.addSpanWatchers(sp);

            if (mTransformation != null) {
                sp.setSpan(mTransformation, 0, textLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }

            if (mMovement != null) {
                mMovement.initialize(this, (Spannable) text);

                /*
                 * Initializing the movement method will have set the
                 * selection, so reset mSelectionMoved to keep that from
                 * interfering with the normal on-focus selection-setting.
                 */
                if (mEditor != null) mEditor.mSelectionMoved = false;
            }
        }

        if (mLayout != null) {
            checkForRelayout();
        }

        sendOnTextChanged(text, 0, oldlen, textLength);
        onTextChanged(text, 0, oldlen, textLength);

        notifyViewAccessibilityStateChangedIfNeeded(AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT);

        if (needEditableForNotification) {
            sendAfterTextChanged((Editable) text);
        }

        // SelectionModifierCursorController depends on textCanBeSelected, which depends on text
        if (mEditor != null) mEditor.prepareCursorControllers();
    }
```

从上面的源代码中我们可以看到下面几个问题：
 A. 代码很长（好重要的感悟啊！！！）
 B. 判断type的时候，在一些条件下会重新构造Spannable，至于构造Editable咋们就无视掉。
 C.对text移除ChangeWatcher(继承TextWatcher,SpanWatcher)，重新添加ChangeWatcher

**我们要的只是一个高效率展示复杂文本的TextView，不需要Editable，也不需要ChangeWatcher。更重要的我们如果能先计算好布局，省去settext这个复杂过程，直接给View调用这样性能提升会更快。**



-  Android提供了文本布局的基类android.text.Layout，注意看Layout的类注释：

```dart
/**
 * A base class that manages text layout in visual elements on
 * the screen.
 * <p>For text that will be edited, use a {@link DynamicLayout},
 * which will be updated as the text changes.
 * For text that will not change, use a {@link StaticLayout}.
 */
public abstract class Layout{...}
```

***注释中提到：对于可编辑的文本，使用DynamicLayout，当文本变化的时候会执行update。而对于静态文本，推荐使用StaticLayout.\***

- 翻过来看TextView怎么做的了？
  TextView包含了3种布局:
   a. BoringLayout (单行纯文字文本)
   b. Staticlayout   (多行复杂文本）
   c. DynamicLayout (多行可编辑复杂文本）
   从TextView的设计上来说，这个设计应该能满足我们的多行静态复杂文本的展示啊**（判断完逻辑采用Staticlayout啊！！！）**，但是阅读下源码，你会发现，TextView构造一个layout，只要文本中有Spannable的出现，TextView就会构造DynamicLayout来负责文本展示。

```java
protected Layout makeSingleLayout(int wantWidth, BoringLayout.Metrics boring, int ellipsisWidth,
            Layout.Alignment alignment, boolean shouldEllipsize, TruncateAt effectiveEllipsize,
            boolean useSaved) {
        Layout result = null;
        if (mText instanceof Spannable) {  //!!!!!!!注意看这里！！！！
            result = new DynamicLayout(mText, mTransformed, mTextPaint, wantWidth,
                    alignment, mTextDir, mSpacingMult, mSpacingAdd, mIncludePad,
                    mBreakStrategy, mHyphenationFrequency,
                    getKeyListener() == null ? effectiveEllipsize : null, ellipsisWidth);
        } else {
            if (boring == UNKNOWN_BORING) {
                boring = BoringLayout.isBoring(mTransformed, mTextPaint, mTextDir, mBoring);
                if (boring != null) {
                    mBoring = boring;
                }
            }

            if (boring != null) {
                if (boring.width <= wantWidth &&
                        (effectiveEllipsize == null || boring.width <= ellipsisWidth)) {
                    if (useSaved && mSavedLayout != null) {
                        result = mSavedLayout.replaceOrMake(mTransformed, mTextPaint,
                                wantWidth, alignment, mSpacingMult, mSpacingAdd,
                                boring, mIncludePad);
                    } else {
                        result = BoringLayout.make(mTransformed, mTextPaint,
                                wantWidth, alignment, mSpacingMult, mSpacingAdd,
                                boring, mIncludePad);
                    }

                    if (useSaved) {
                        mSavedLayout = (BoringLayout) result;
                    }
                } else if (shouldEllipsize && boring.width <= wantWidth) {
                    if (useSaved && mSavedLayout != null) {
                        result = mSavedLayout.replaceOrMake(mTransformed, mTextPaint,
                                wantWidth, alignment, mSpacingMult, mSpacingAdd,
                                boring, mIncludePad, effectiveEllipsize,
                                ellipsisWidth);
                    } else {
                        result = BoringLayout.make(mTransformed, mTextPaint,
                                wantWidth, alignment, mSpacingMult, mSpacingAdd,
                                boring, mIncludePad, effectiveEllipsize,
                                ellipsisWidth);
                    }
                }
            }
        }
        if (result == null) {
            StaticLayout.Builder builder = StaticLayout.Builder.obtain(mTransformed,
                    0, mTransformed.length(), mTextPaint, wantWidth)
                    .setAlignment(alignment)
                    .setTextDirection(mTextDir)
                    .setLineSpacing(mSpacingAdd, mSpacingMult)
                    .setIncludePad(mIncludePad)
                    .setBreakStrategy(mBreakStrategy)
                    .setHyphenationFrequency(mHyphenationFrequency);
            if (shouldEllipsize) {
                builder.setEllipsize(effectiveEllipsize)
                        .setEllipsizedWidth(ellipsisWidth)
                        .setMaxLines(mMaxMode == LINES ? mMaximum : Integer.MAX_VALUE);
            }
            // TODO: explore always setting maxLines
            result = builder.build();
        }
        return result;
    }
```

**StaticLayout表示不服，Spannable我也能处理，而且我简单效率高。**Google为什么这么设计，可能是因为TextView作为Button, CheckedTextView, EditText, Switch,ToggleButton等的基类表示压力山大吧！有那位同学给我解析下为啥TextView要把EditText这个子类关于edit相关的活自己干！

### 找到了解决问题的方式，下面就是纯撸代码了

- 1 自定义StaticLayoutView



```java
public class StaticLayoutView extends View {

    private Layout layout;

    private int width ;
    private int height;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StaticLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public StaticLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StaticLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticLayoutView(Context context) {
        super(context);
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        if (this.layout.getWidth() != width || this.layout.getHeight() != height) {
            width = this.layout.getWidth();
            height = this.layout.getHeight();
            requestLayout();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (layout != null) {
            layout.draw(canvas, null, null, 0);
        }
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (layout != null) {
            setMeasuredDimension(layout.getWidth(), layout.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
```

- 2 StaticLayout [父类Layout]的一个构造函数



```dart
    /**
     * Subclasses of Layout use this constructor to set the display text,
     * width, and other standard properties.
     * @param text the text to render
     * @param paint the default paint for the layout.  Styles can override
     * various attributes of the paint.
     * @param width the wrapping width for the text.
     * @param align whether to left, right, or center the text.  Styles can
     * override the alignment.
     * @param spacingMult factor by which to scale the font size to get the
     * default line spacing
     * @param spacingAdd amount to add to the default line spacing
     *
     * @hide
     */
    protected Layout(CharSequence text, TextPaint paint,
                     int width, Alignment align, TextDirectionHeuristic textDir,
                     float spacingMult, float spacingAdd) {
```

> a. 构造CharSequence，平时我们对复杂文本的处理，就是通过spannerString来构造，比如某段用什么颜色，某段用什么size
>  b. 构造TextPaint，可以设置整个文本的字体size,color等（当然spannerString来分段控制更好），也可以来设置字体的type等
>  c. 输入其他参数，构造完整个StaticLayout后,调用StaticLayoutView的
>  setLayout方法就完成了整个绘制，这个构造的过程当然也可以放在子线程来做。

- 3 细节问题
   细节永远是最麻烦的事情，比如你项目有默认设定的复杂文本的颜色，文字size，阴影等等，不像textview天生就提供textsize,textcolor,shadow这些属性，你的选择有2个。第一，自己构造这些属性，第二，围绕你的CharSequence和textpaint构造更多方便实现你业务ui效果的方法。

- 4 StaticLayout的用途
   a.文中高频度大量textview刷新优化。
   b.一个textview显示大量的文本，比如一些阅读app。
   c. 在控件上画文本，比如一个ImageView中心画文本。
   d. 一些排版效果,比如多行文本文字居中对齐等。
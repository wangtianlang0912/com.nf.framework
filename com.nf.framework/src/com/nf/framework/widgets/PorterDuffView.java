package com.nf.framework.widgets;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

/**
 * 自定义组件实现新浪微博的图片加载效果。<br/>
 * 
 * 
 * 
 */
public class PorterDuffView extends ImageView {
		private static final String TAG="PorterDuffView";
        /** 前景Bitmap高度为1像素。采用循环多次填充进度区域。 */
        public static final int FG_HEIGHT = 1;
        /** 下载进度前景色 */
        // public static final int FOREGROUND_COLOR = 0x77123456;
        public static final int FOREGROUND_COLOR = 0x00ffffff;
        /** 下载进度条的颜色。 */
        public static final int TEXT_COLOR = 0x00666666;
        /** 进度百分比字体大小。 */
        public static final int FONT_SIZE = 30;
        private Bitmap bitmapBg, bitmapFg;
        private Paint paint;
        private Context mcontext;
        /** 标识当前进度。 */
        private float progress;
        /** 标识进度图片的宽度与高度。 */
        private int width, height;
        /** 格式化输出百分比。 */
        private DecimalFormat decFormat;
        /** 进度百分比文本的锚定Y中心坐标值。 */
        private float txtBaseY;
        /** 标识是否使用PorterDuff模式重组界面。 */
        private boolean porterduffMode=true;
        /** 标识是否正在下载图片。 */
        private boolean loading;

        public PorterDuffView(Context context, AttributeSet attrs) {
                super(context, attrs);
                init(context, attrs);
        }

        public PorterDuffView(Context context) {
			// TODO Auto-generated constructor stub
        	 super(context);
             init(context, null);
		}

		/** 生成一宽与背景图片等同高为1像素的Bitmap，。 */
        private static Bitmap createForegroundBitmap(int w) {
                Bitmap bm = Bitmap.createBitmap(w, FG_HEIGHT, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bm);
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setColor(FOREGROUND_COLOR);
                
                c.drawRect(0, 0, 600, FG_HEIGHT, p);
                return bm;
        }

        private void init(Context context, AttributeSet attrs) {
        		mcontext=context;   
        		if (attrs != null) {
                        // //////////////////////////////////////////
                        // int count = attrs.getAttributeCount();
                        // for (int i = 0; i < count; i++) {
                        // LogOut.out(this, "attrNameRes:" +
                        // Integer.toHexString(attrs.getAttributeNameResource(i))//
                        // + " attrName:" + attrs.getAttributeName(i)//
                        // + " attrResValue:" + attrs.getAttributeResourceValue(i, -1)//
                        // + " attrValue:" + attrs.getAttributeValue(i)//
                        // );
                        // }
                        // //////////////////////////////////////////
                	porterduffMode=true;
//                        TypedArray typedArr = context.obtainStyledAttributes(attrs, R.styleable.PorterDuffView);
//                        porterduffMode = typedArr.getBoolean(R.styleable.PorterDuffView_porterduffMode, false);
                }
                Drawable drawable = getDrawable();
                if (porterduffMode && drawable != null && drawable instanceof BitmapDrawable) {
                        bitmapBg = ((BitmapDrawable) drawable).getBitmap();
                        width = bitmapBg.getWidth();
                        height = bitmapBg.getHeight();
                        
                        // LogOut.out(this, "width=" + width + " height=" + height);
                        bitmapFg = createForegroundBitmap(width);
                } else {
                        // 不符合要求，自动设置为false。
                        porterduffMode = false;
                }

                paint = new Paint();
                paint.setFilterBitmap(false);
                paint.setAntiAlias(true);
                paint.setTextSize(FONT_SIZE);

                // 关于FontMetrics的详情介绍，可见：
                // <a href="\"http://xxxxxfsadf.iteye.com/blog/480454\"" target="\"_blank\"">http://xxxxxfsadf.iteye.com/blog/480454</a>
                Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                // 注意观察本输出：
                // ascent:单个字符基线以上的推荐间距，为负数
                Log.i(TAG, "ascent:" + fontMetrics.ascent//
                                // descent:单个字符基线以下的推荐间距，为正数
                                + " descent:" + fontMetrics.descent //
                                // 单个字符基线以上的最大间距，为负数
                                + " top:" + fontMetrics.top //
                                // 单个字符基线以下的最大间距，为正数
                                + " bottom:" + fontMetrics.bottom//
                                // 文本行与行之间的推荐间距
                                + " leading:" + fontMetrics.leading);
                // 在此处直接计算出来，避免了在onDraw()处的重复计算
                txtBaseY = (height - fontMetrics.bottom - fontMetrics.top) / 2;

                decFormat = new DecimalFormat("0.0%");
        }
        
        public void onDraw(Canvas canvas) {
                if (porterduffMode) {
                        int tmpW = (getWidth() - width)/2, tmpH = (getHeight() - height)/2;
                        // 画出背景图
                        canvas.drawBitmap(bitmapBg, tmpW, tmpH, paint);
                        // 设置PorterDuff模式
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
//                      canvas.drawBitmap(bitmapFg, tmpW, tmpH - progress * height, paint);
                        int tH = height - (int) (progress * height);
                        for (int i = 0; i < tH; i++) {
                             canvas.drawBitmap(bitmapFg, tmpW, tmpH + i, paint);
                        }
                        // 立即取消xfermode
                        paint.setXfermode(null);
                        int oriColor = paint.getColor();
                        paint.setColor(TEXT_COLOR);
                        paint.setTextSize(FONT_SIZE);
                        String tmp = decFormat.format(progress);
                        float tmpWidth = paint.measureText(tmp);
                        canvas.drawText(decFormat.format(progress), tmpW + (width - tmpWidth) / 2, tmpH + txtBaseY, paint);
                        // 恢复为初始值时的颜色
                        paint.setColor(oriColor);
                } else {
                        super.onDraw(canvas);
                }
        }

        public void setProgress(float progress) {
                if (porterduffMode) {
                        this.progress = progress;
                        // 刷新自身。
                        invalidate();
                }
        }

        public void setBitmap(Bitmap bg) {
        		if (porterduffMode&&bg!=null) {
                        bitmapBg = bg;
                        Display display =((Activity)mcontext).getWindowManager().getDefaultDisplay();
                			DisplayMetrics metric = new DisplayMetrics();
                			display.getMetrics(metric);
                        width = (int) (bitmapBg.getWidth()*metric.density);
                        height = (int) (bitmapBg.getHeight()*metric.density);
                      
                        bitmapFg = createForegroundBitmap(width);
                        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                        txtBaseY = (height - fontMetrics.bottom - fontMetrics.top) / 2;
                        setImageBitmap(bg);
                        // 请求重新布局，将会再次调用onMeasure()
                        requestLayout();
                        bg=null;
                }
        }

        public boolean isLoading() {
                return loading;
        }

        public void setLoading(boolean loading) {
                this.loading = loading;
        }

        public void setPorterDuffMode(boolean bool) {
                porterduffMode = bool;
        }
}

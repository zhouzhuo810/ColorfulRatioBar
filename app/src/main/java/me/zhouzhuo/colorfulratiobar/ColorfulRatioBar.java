package me.zhouzhuo.colorfulratiobar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import me.zhouzhuo.colorfulratiobar.utils.ScreenUtils;


/**
 * 彩色比例条
 * @author Zz
 * @date 2015/11/8 11:45
 * @email  admin@zhouzhuo.me
 */
public class ColorfulRatioBar extends View {

    //天蓝色
    private static final int DEFAULT_COLOR_ONE = 0x4fc1e9;
    //灰色
    private static final int DEFAULT_COLOR_TWO = 0xd7d8dd;
    //黄色
    private static final int DEFAULT_COLOR_THREE = 0xffce54;
    //绿色
    private static final int DEFAULT_COLOR_FOUR = 0xa0d468;
    //红色
    private static final int DEFAULT_COLOR_FIVE = 0xed5565;
    //默认总数
    private static final int DEFAULT_COUNT = 4;
    //默认比例
    private static final int DEFAULT_RATIO = 0;
    //默认倾斜度（4px）
    private static final int DEFAULT_DEGREE = 4;
    //数字
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    //默认宽度
    private int width;
    //默认高度
    private int height = 10;
    //总数，可选3 4 5
    private int count;
    //倾斜度
    private int degree;
    //比例1
    private int ratioOne;
    //比例2
    private int ratioTwo;
    //比例3
    private int ratioThree;
    //比例4
    private int ratioFour;
    //比例5
    private int ratioFive;
    //颜色1
    private int colorOne;
    //颜色2
    private int colorTwo;
    //颜色3
    private int colorThree;
    //颜色4
    private int colorFour;
    //颜色5
    private int colorFive;
    //画笔
    private Paint paint;
    //路径
    private Path pathOne;
    private Path pathTwo;
    private Path pathThree;
    private Path pathFour;
    private Path pathFive;
    //比例和
    private int ratioSum;
    //各比例横坐标值
    private float fourWidthTop;
    private float fourWidthBottom;
    private float threeWidthTop;
    private float threeWidthBottom;
    private float twoWidthTop;
    private float twoWidthBottom;
    private float oneWidthTop;
    private float oneWidthBottom;

    public ColorfulRatioBar(Context context) {
        super(context);
        initView(context);
        initData(context);
    }

    public ColorfulRatioBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        initData(context);
    }

    public ColorfulRatioBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData(context);
    }

    /**
     * 默认初始化资源
     * @param context
     */
    private void initView(Context context) {
        //初始化画笔
        paint = new Paint();
        //初始化路径
        pathOne = new Path();
        pathTwo = new Path();
        pathThree = new Path();
        pathFour = new Path();
        pathFive = new Path();

        count = DEFAULT_COUNT;
        degree = DEFAULT_DEGREE;

        ratioOne = DEFAULT_RATIO;
        ratioTwo = DEFAULT_RATIO;
        ratioThree = DEFAULT_RATIO;
        ratioFour = DEFAULT_RATIO;
        ratioFive = DEFAULT_RATIO;

        colorOne = DEFAULT_COLOR_ONE;
        colorTwo = DEFAULT_COLOR_TWO;
        colorThree = DEFAULT_COLOR_THREE;
        colorFour = DEFAULT_COLOR_FOUR;
        colorFive = DEFAULT_COLOR_FIVE;

    }

    /**
     * 初始化资源
     */
    private void initView(Context context, AttributeSet attrs) {
        //初始化画笔
        paint = new Paint();
        //初始化路径
        pathOne = new Path();
        pathTwo = new Path();
        pathThree = new Path();
        pathFour = new Path();
        pathFive = new Path();
        //绑定属性资源
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ColorfulRatioBar);
        //获取属性值
        //获取比例个数
        this.count = array.getInt(R.styleable.ColorfulRatioBar_count, DEFAULT_COUNT);
        if (count >= 3 && count <= 5) {
            //获取倾斜度
            this.degree = array.getDimensionPixelSize(R.styleable.ColorfulRatioBar_degree, DEFAULT_DEGREE);
            //初始化比例
            this.ratioOne = array.getInt(R.styleable.ColorfulRatioBar_ratioOne, DEFAULT_RATIO);
            this.ratioTwo = array.getInt(R.styleable.ColorfulRatioBar_ratioTwo, DEFAULT_RATIO);
            this.ratioThree = array.getInt(R.styleable.ColorfulRatioBar_ratioThree, DEFAULT_RATIO);
            this.ratioFour = array.getInt(R.styleable.ColorfulRatioBar_ratioFour, DEFAULT_RATIO);
            this.ratioFive = array.getInt(R.styleable.ColorfulRatioBar_ratioFive, DEFAULT_RATIO);
            //初始化颜色
            this.colorOne = array.getColor(R.styleable.ColorfulRatioBar_colorOne, DEFAULT_COLOR_ONE);
            this.colorTwo = array.getColor(R.styleable.ColorfulRatioBar_colorTwo, DEFAULT_COLOR_TWO);
            this.colorThree = array.getColor(R.styleable.ColorfulRatioBar_colorThree, DEFAULT_COLOR_THREE);
            this.colorFour = array.getColor(R.styleable.ColorfulRatioBar_colorFour, DEFAULT_COLOR_FOUR);
            this.colorFive = array.getColor(R.styleable.ColorfulRatioBar_colorFive, DEFAULT_COLOR_FIVE);

        }
        //释放资源
        array.recycle();
    }

    /**
     * 初始化绘图数据
     */
    private void initData(Context context) {
        width = ScreenUtils.getScreenWidth(context);
        if (getWidth() != 0) {
            width = getWidth();
        }
        exactCompute();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画笔样式
        paint.setStyle(Paint.Style.FILL);
        //抗锯齿
        paint.setAntiAlias(true);
        if (ratioSum == 0) {
            drawColorfulLine(canvas, pathOne, colorOne, 0, width, width, 0, 0, height);
        }else if (ratioSum > 0){
            //画比例线条1
            drawColorfulLine(canvas, pathOne, colorOne, 0, oneWidthTop, width, 0, 0, height);
            //画比例线条2
            drawColorfulLine(canvas, pathTwo, colorTwo, oneWidthTop, twoWidthTop, width, oneWidthBottom, 0 , height);
            //画比例线条3
            drawColorfulLine(canvas, pathThree, colorThree, twoWidthTop, threeWidthTop, width, twoWidthBottom, 0 , height);
            //画比例线条4
            drawColorfulLine(canvas, pathFour, colorFour, threeWidthTop, fourWidthTop, width, threeWidthBottom, 0 , height);
            //画比例线条5
            drawColorfulLine(canvas, pathFive, colorFive, fourWidthTop, width, width, fourWidthBottom, 0 , height);
        }
    }

    /**
     * 画比例条的某个比例部分
     * @param canvas
     * @param path
     * @param color
     * @param x1
     * @param x2
     * @param x3
     * @param x4
     * @param y1
     * @param y2
     */
    private void drawColorfulLine(Canvas canvas, Path path, int color, float x1, float x2,float x3, float x4, float y1, float y2) {
        paint.setColor(color);
        path.moveTo(x1, y1);
        path.lineTo(x2, y1);
        path.lineTo(x3, y2);
        path.lineTo(x4, y2);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 更新数据
     */
    public void updateData() {
        exactCompute();
        postInvalidate();
    }

    /**
     * 精确计算各个坐标
     */
    private void exactCompute() {
        //求比例和
        ratioSum = ratioOne + ratioTwo + ratioThree + ratioFour + ratioFive;
        if (ratioSum != 0) {
            //求各比例对应屏幕宽度
            oneWidthTop = width * (ratioOne * 1.0f / ratioSum ) + degree;
            oneWidthBottom = width * (ratioOne * 1.0f / ratioSum ) - degree;
            twoWidthTop = width * ((ratioOne + ratioTwo) * 1.0f / ratioSum) + degree;
            twoWidthBottom = width * ((ratioOne + ratioTwo) * 1.0f / ratioSum ) - degree;
            threeWidthTop = width * ((ratioOne + ratioTwo + ratioThree) * 1.0f / ratioSum ) + degree;
            threeWidthBottom = width * ((ratioOne + ratioTwo + ratioThree) * 1.0f / ratioSum ) - degree;
            fourWidthTop = width * ((ratioOne + ratioTwo + ratioThree + ratioFour) * 1.0f / ratioSum )+ degree;
            fourWidthBottom = width * ((ratioOne + ratioTwo + ratioThree + ratioFour) * 1.0f / ratioSum )- degree;
        }
        //如果比例5为0
        if (ratioFive == 0) {
            //如果比例4为0
            if (ratioFour == 0) {
                //如果比例3为0
                if (ratioThree == 0) {
                    if (ratioTwo == 0) {
                        if (ratioOne != 0) {
                            //比例2，3，4，5为0，比例1不为0
                            oneWidthBottom = oneWidthTop;
                        }
                    }else {
                        //比例3，4，5为0，比例2不为0
                        twoWidthBottom = twoWidthTop;
                    }
                }else {
                    //比例4，5为0，比例3不为0
                    threeWidthBottom = threeWidthTop;
                }
            }else {
                //比例5为0，比例4不为0
                fourWidthBottom = fourWidthTop;
            }
        }
    }

    /**
     * 获取比例种类数
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置比例种类数
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 获取比例1的比例
     * @return
     */
    public int getRatioOne() {
        return ratioOne;
    }

    /**
     * 设置比例1的比例
     * @param ratioOne
     */
    public void setRatioOne(int ratioOne) {
        this.ratioOne = ratioOne;
        updateData();
    }

    /**
     * 获取比例2的比例
     * @return
     */
    public int getRatioTwo() {
        return ratioTwo;
    }

    /**
     * 设置比例2的比例
     * @param ratioTwo
     */
    public void setRatioTwo(int ratioTwo) {
        this.ratioTwo = ratioTwo;
        updateData();
    }

    /**
     * 获取比例3的比例
     * @return
     */
    public int getRatioThree() {
        return ratioThree;
    }

    /**
     * 设置比例3的比例
     * @param ratioThree
     */
    public void setRatioThree(int ratioThree) {
        this.ratioThree = ratioThree;
        updateData();
    }

    /**
     * 获取比例4的比例
     * @return
     */
    public int getRatioFour() {
        return ratioFour;
    }

    /**
     * 设置比例4的比例
     * @param ratioFour
     */
    public void setRatioFour(int ratioFour) {
        this.ratioFour = ratioFour;
        updateData();
    }

    /**
     * 获取比例5的比例
     * @return
     */
    public int getRatioFive() {
        return ratioFive;
    }

    /**
     * 设置比例5的比例
     * @param ratioFive
     */
    public void setRatioFive(int ratioFive) {
        this.ratioFive = ratioFive;
        updateData();
    }

    /**
     * 获取比例1的颜色
     * @return
     */
    public int getColorOne() {
        return colorOne;
    }

    /**
     * 设置比例1的颜色
     * @param colorOne
     */
    public void setColorOne(int colorOne) {
        this.colorOne = colorOne;
        postInvalidate();
    }

    /**
     * 获取比例2的颜色
     * @return
     */
    public int getColorTwo() {
        return colorTwo;
    }

    /**
     * 设置比例2的颜色
     * @param colorTwo
     */
    public void setColorTwo(int colorTwo) {
        this.colorTwo = colorTwo;
        postInvalidate();
    }

    /**
     * 获取比例3的颜色
     * @return
     */
    public int getColorThree() {
        return colorThree;
    }

    /**
     * 设置比例3的颜色
     * @param colorThree
     */
    public void setColorThree(int colorThree) {
        this.colorThree = colorThree;
        postInvalidate();
    }

    /**
     * 获取比例4的颜色
     * @return
     */
    public int getColorFour() {
        return colorFour;
    }

    /**
     * 设置比例4的颜色
     * @param colorFour
     */
    public void setColorFour(int colorFour) {
        this.colorFour = colorFour;
        postInvalidate();
    }

    /**
     * 获取比例5的颜色
     * @return
     */
    public int getColorFive() {
        return colorFive;
    }

    /**
     * 设置比例5的颜色
     * @param colorFive
     */
    public void setColorFive(int colorFive) {
        this.colorFive = colorFive;
        postInvalidate();
    }

    /**
     * 获取倾斜度
     * @return
     */
    public int getDegree() {
        return degree;
    }

    /**
     * 设置倾斜度
     * @param degree
     */
    public void setDegree(int degree) {
        this.degree = degree;
        updateData();
    }

    /**
     * 一次性设置多个比例(3~5个)
     * @param ratios
     */
    public void setRatio(int... ratios) {
        int count = ratios.length;
        switch (count) {
            case THREE:
                this.ratioOne = ratios[0];
                this.ratioTwo = ratios[1];
                this.ratioThree = ratios[2];
                updateData();
                break;
            case FOUR:
                this.ratioOne = ratios[0];
                this.ratioTwo = ratios[1];
                this.ratioThree = ratios[2];
                this.ratioFour = ratios[3];
                updateData();
                break;
            case FIVE:
                this.ratioOne = ratios[0];
                this.ratioTwo = ratios[1];
                this.ratioThree = ratios[2];
                this.ratioFour = ratios[3];
                this.ratioFive = ratios[4];
                updateData();
                break;
            default:
                break;
        }
    }
    /**
     * 一次性设置多个颜色(3~5个)
     * @param colors
     */
    public void setColors(int... colors) {
        int count = colors.length;
        switch (count) {
            case THREE:
                this.colorOne = colors[0];
                this.colorTwo = colors[1];
                this.colorThree = colors[2];
                updateData();
                break;
            case FOUR:
                this.colorOne = colors[0];
                this.colorTwo = colors[1];
                this.colorThree = colors[2];
                this.colorFour = colors[3];
                updateData();
                break;
            case FIVE:
                this.colorOne = colors[0];
                this.colorTwo = colors[1];
                this.colorThree = colors[2];
                this.colorFour = colors[3];
                this.colorFive = colors[4];
                updateData();
                break;
            default:
                break;
        }
    }


}

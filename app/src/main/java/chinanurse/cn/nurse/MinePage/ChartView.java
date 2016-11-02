package chinanurse.cn.nurse.MinePage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class ChartView extends View {

    Paint paint, paint1;

    public int XPoint = 40;    //原点的X坐标
    public int YPoint = 160;     //原点的Y坐标
//    public int XScale = 55;     //X的刻度长度
//    public int YScale = 40;     //Y的刻度长度
//    public int XLength = 380;        //X轴的长度
//    public int YLength = 240;        //Y轴的长度

    int XScale, YScale;
    int XLength, YLength;


    public String[] XLabel;    //X的刻度
    public String[] YLabel;    //Y的刻度
    public int[] Data;      //数据
    public String Title;    //显示的标题
    private Context context;


    public ChartView(Context context, String[] XLabel, String[] YLabel, int[] Data, String Title, int XScale, int YScale) {
        super(context);
        this.context = context;
        this.XLabel = XLabel;
        this.YLabel = YLabel;
        this.Data = Data;
        this.Title = Title;
        this.XScale = XScale;
        this.YScale = YScale;
        this.XLength = XLabel.length * XScale;
        this.YLength = YLabel.length * YScale;

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//去锯齿
        paint.setColor(Color.BLUE);//颜色
        paint.setTextSize(15);  //设置轴文字大小

        paint1 = new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);//去锯齿
        paint1.setColor(Color.GREEN);
        paint1.setTextSize(16);


    }


    //  2.构造函数问题：自定义一个View，必须派生实现基类View的三个构造函数
//    View(Context context)     //Simple constructor to use when creating a view from code
//
//    View(Context context, AttributeSet attrs)     //Constructor that is called when inflating a view from XML
//
//    View(Context context, AttributeSet attrs, int defStyle)     //Perform inflation from XML and apply a class-specific base style
//
//    从文档上的介绍来看，第二个和第三个构造函数对于XML这种引用方式是必须实现的，这三个构造函数应该是在不同的应用场合来实例化一个View对象。

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init_View(Context context, String[] XLabel, String[] YLabel, int[] Data, String Title, int XScale, int YScale) {
        this.context = context;
        this.XLabel = XLabel;
        this.YLabel = YLabel;
        this.Data = Data;
        this.Title = Title;
        this.XScale = XScale;
        this.YScale = YScale;
        this.XLength = XLabel.length * XScale;
        this.YLength = YLabel.length * YScale;

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//去锯齿
        paint.setColor(Color.WHITE);//颜色
        paint.setTextSize(20);  //设置轴文字大小

        paint1 = new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);//去锯齿
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(20);


    }


    public void setPoint(int XPoint, int YPoint) {
        this.XPoint = XPoint;
        this.YPoint = YPoint;

    }

    public void setPointData(int[] Data) {
        this.Data = Data;
    }

    public void setXLabel(String[] XLabel) {
        this.XLabel = XLabel;
        this.XLength = XLabel.length * XScale;
    }

    public void setYLabel(String[] YLabel) {
        this.XLabel = YLabel;
        this.YLength = YLabel.length * YScale;
    }


    public void set_Line_Paint(Paint paint) {

        this.paint = paint;
    }

    public void set_Text_Paint(Paint paint1) {

        this.paint1 = paint1;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//重写onDraw方法
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//去锯齿
        paint.setColor(Color.BLACK);//颜色
        paint.setTextSize(15);  //设置轴文字大小

        paint1 = new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);//去锯齿
        paint1.setColor(Color.WHITE);
        paint1.setTextSize(16);
       if ("".equals(XPoint)||"".equals(YLength)||"".equals(YPoint)||"".equals(paint)){
           //设置Y轴
           Log.e("XPoint","----------->"+XPoint);
           Log.e("YPoint - YLength","----------->"+(YPoint - YLength));
           Log.e("XPoint","----------->"+XPoint);
           Log.e("YPoint","----------->"+YPoint);
           Log.e("paint","----------->"+paint);
           canvas.drawLine(60, 60, 60, 300, paint);   //轴线
       }else {
           Log.e("XPoint","----------->"+XPoint);
           Log.e("YPoint - YLength","----------->"+(YPoint - YLength));
           Log.e("XPoint","----------->"+XPoint);
           Log.e("YPoint","----------->"+YPoint);
           Log.e("paint","----------->"+paint);
           //设置Y轴
           canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint);   //轴线
       }
        for (int i = 0; i * YScale < YLength; i++) {
            canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i * YScale, paint);  //刻度
            try {
//                if (i == 0) {
//
//                    canvas.drawText(YLabel[i], XPoint - 35, YPoint - i * YScale + 5, paint);  //文字
//                } else {
                canvas.drawText(YLabel[i] + "%", XPoint - 45, YPoint - i * YScale + 5, paint);  //文字
//                }
            } catch (Exception e) {
            }
        }
        canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength + 6, paint);  //箭头
        canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength + 6, paint);
        //设置X轴
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);   //轴线
        for (int i = 0; i * XScale < XLength; i++) {
            canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale, YPoint - 5, paint);  //刻度
            try {
                canvas.drawText(XLabel[i], XPoint + i * XScale - 10, YPoint + 20, paint);  //文字
                //数据值
                if (i > 0 && YCoord(Data[i - 1]) != -999 && YCoord(Data[i]) != -999)  //保证有效数据
                    canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data[i - 1]), XPoint + i * XScale, YCoord(Data[i]), paint1);
                canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 2, paint1);
            } catch (Exception e) {
            }
        }
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint - 3, paint);    //箭头
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint + 3, paint);
        paint.setTextSize(16);
        if (Title == null||Title.length() <= 0){
            canvas.drawText("", 150, 50, paint);
        }else{
            canvas.drawText(Title, 150, 50, paint);
        }

    }

    private int YCoord(int y0)  //计算绘制时的Y坐标，无数据时返回-999
    {
        int y;
        try {
//            y = Integer.parseInt(y0);

            y = y0;
        } catch (Exception e) {
            return -999;    //出错则返回-999
        }
        try {
            return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
        } catch (Exception e) {
        }
        return y;
    }
}

package com.jaccen.androidlearn.SurfaceView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

import com.jaccen.androidlearn.R;
import com.jaccen.androidlearn.SurfaceView.OpenGL.MatrixState;
import com.jaccen.androidlearn.SurfaceView.OpenGL.TextureRect;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 自定义显示 OpenGL 图形的 SurfaceView
 *
 * ① 初始化 SurfaceView
 * 		a. 设置 OpenGL ES 版本
 * 		b. 创建场景渲染器
 * 		c. 设置场景渲染器
 * 		d. 设置场景渲染器模式
 * ② 自定义场景渲染器
 * 		a. 创建时 设置背景 -> 创建绘制元素 -> 打开深度检测
 * 		b. 场景改变时 设置视口参数 -> 设置投影参数 -> 设置摄像机参数
 * 		c. 绘制时 清楚颜色,深度缓冲 -> 绘制元素
 */
public class MyGLSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320; 	// 角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器

    private float mPreviousY;								//上次触摸位置的Y坐标
    private float mPreviousX;
    int textureCTId;//系统分配的拉伸纹理id
    int textureREId;//系统分配的重复纹理id
    int currTextureId;//当前纹理id

    TextureRect[] texRect=new TextureRect[3];//纹理矩形数组
    int trIndex=0;//当前纹理矩形索引

    public MyGLSurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        //Android GLSurface 除绘制物体外，其余为透明
        setZOrderOnTop(true);
        setEGLConfigChooser(8,8,8,8,24,0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mRenderer = new SceneRenderer();	//创建场景渲染器
        setRenderer(mRenderer);				//设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//设置渲染模式为主动渲染
    }
    // 触摸方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();	//获取当前触摸的 y 坐标
        float x = e.getX();	//获取当前触摸的 x 坐标
        switch (e.getAction()) {	//获取触摸类型
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;// 计算 y 方向的位移
                float dx = x - mPreviousX;// 计算 x 方向的位移
                //可以对顶点进行旋转处理
                //TODO
        }
        mPreviousY = y;// 将本次触摸的 y 坐标记录为历史坐标
        mPreviousX = x;// 将本次触摸的 x 坐标记录为历史坐标
        return true;
    }
    /**
     * 场景渲染器
     *
     */
    private class SceneRenderer implements GLSurfaceView.Renderer
    {
        /**
         * ① 清楚深度缓冲 与 颜色缓冲
         * ② 重新绘制各个元素
         */
        public void onDrawFrame(GL10 gl)
        {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glClearColor(0.0f, 1.0f, 0.0f,1.0f);
			/*if (romLoaded) {
            	Bitmap localBitmap = Bitmap.createBitmap(240, 160, Bitmap.Config.RGB_565);
	            EmulatorGL20View.e.position(0);
	            localBitmap.copyPixelsFromBuffer(EmulatorGL20View.e);
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, localBitmap, 0);
                //GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, localBitmap);

                localBitmap.recycle();
			}*/

            //绘制当前纹理矩形
            texRect[trIndex].drawSelf(currTextureId);
        }

        /**
         * Surface 改变时
         * ① 设置视口参数
         * ② 设置投影参数
         * ③ 设置摄像机参数
         */
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            // 设置视口的宽高比, 注意视口的长宽比与近平面的长宽比需要相同, 否则显示内容会变形
            float ratio = (float) width / height;
            float near = 1.0f;
            float far = 1000.0f;
            float cameraZ = 10.0f;
            //设置透视投影
            MatrixState.setProjectFrustum(-ratio , ratio, -1 , 1 , near, far);


            //调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0, 0, cameraZ, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            //    texRect[trIndex].setRatio(ratio, cameraZ - near - 0.000001f);//
            texRect[trIndex].initVertexData();
        }
        /**
         * 创建时回调
         * ① 设置背景颜色
         * ② 创建绘制元素
         * ③ 打开深度检测
         */
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //设置屏幕背景色RGBA
            GLES20.glClearColor(1.0f,0.0f,0.0f, 1.0f);
            //创建三个纹理矩形对对象
            texRect[0]=new TextureRect(MyGLSurfaceView.this);
            texRect[1]=new TextureRect(MyGLSurfaceView.this);
            texRect[2]=new TextureRect(MyGLSurfaceView.this);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //初始化系统分配的拉伸纹理id
            textureCTId = initTexture(null,false);
            //初始化系统分配的重复纹理id
            textureREId = initTexture(null, true);
            //初始化当前纹理id
            currTextureId = textureREId;
            //关闭背面剪裁
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }
    }

    //初始化纹理的方法
    //    isRepeat ture表示纹理是repeat
    public int initTexture(Bitmap bmp, boolean isRepeat)//textureId
    {
        //生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures
                (
                        1,          //产生的纹理id的数量
                        textures,   //纹理id的数组
                        0           //偏移量
                );
        int textureId=textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);


        if(isRepeat)
        {
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        }
        else
        {
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        }
        Bitmap bitmapTmp;
        if (bmp == null)
        {
            //通过输入流加载图片===============begin===================
            @SuppressLint("ResourceType") InputStream is = this.getResources().openRawResource(R.drawable.ar_poi_item);

            try
            {
                bitmapTmp = BitmapFactory.decodeStream(is);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            //通过输入流加载图片===============end=====================

        }
        else
        {
            bitmapTmp = bmp;
        }

//        if (bitmapTmp == null)
//        {
//            bitmapTmp = Bitmap.createBitmap(new int[]{40}, 256, 256, Bitmap.Config.RGB_565);
//        }

        //实际加载纹理
        GLUtils.texImage2D
                (
                        GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                        0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
                        bitmapTmp, 			  //纹理图像
                        0					  //纹理边框尺寸
                );
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片
        return textureId;
    }
}

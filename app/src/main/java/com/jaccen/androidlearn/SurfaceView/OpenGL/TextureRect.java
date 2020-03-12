package com.jaccen.androidlearn.SurfaceView.OpenGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.jaccen.androidlearn.SurfaceView.MyGLSurfaceView;

import static com.jaccen.androidlearn.SurfaceView.OpenGL.ShaderUtil.createProgram;


//纹理矩形
public class TextureRect
{
    int mProgram;//自定义渲染管线程序id
    int muMVPMatrixHandle;//总变换矩阵引用id
    int maPositionHandle; //顶点位置属性引用id
    int maTexCoorHandle; //顶点纹理坐标属性引用id
    String mVertexShader;//顶点着色器
    String mFragmentShader;//片元着色器
    static float[] mMMatrix = new float[16];//具体物体的移动旋转矩阵

    FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer   mTexCoorBuffer;//顶点纹理坐标数据缓冲
    int vCount=0;

    float mRatio = 1.0f;
    float mCordZ = 0.0f;

    public TextureRect(MyGLSurfaceView mv)
    {
        //初始化顶点坐标与着色数据
        initVertexData();
        //初始化shader
        initShader(mv);
    }
    /**
     * 自定义初始化顶点数据的initVertexData方法

     */
    //初始化顶点坐标与着色数据的方法
    public void initVertexData()
    {
        //顶点坐标数据的初始化================begin============================
        vCount=6;
        final float UNIT_SIZE=0.3f;
//        float vertices[]=new float[]
//        {
//        	-4*UNIT_SIZE,4*UNIT_SIZE,0,
//        	-4*UNIT_SIZE,-4*UNIT_SIZE,0,
//        	4*UNIT_SIZE,-4*UNIT_SIZE,0,
//
//        	4*UNIT_SIZE,-4*UNIT_SIZE,0,
//        	4*UNIT_SIZE,4*UNIT_SIZE,0,
//        	-4*UNIT_SIZE,4*UNIT_SIZE,0
//        };
        //float ratio = 0.5625f;
        //float z = 1.999999f;
        float vertices[]=new float[]
                {
                        -mRatio,1.0f,mCordZ,
                        -mRatio,-1.0f,mCordZ,
                        mRatio,-1.0f,mCordZ,

                        mRatio,-1.0f,mCordZ,
                        mRatio,1.0f,mCordZ,
                        -mRatio,1.0f,mCordZ
                };

        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================

        //顶点纹理坐标数据的初始化================begin============================
//        float texCoor[]=new float[]//顶点颜色值数组，每个顶点4个色彩值RGBA
//  	    {
//  	      		0,0, 0,tRange, sRange,tRange,
//  	      		sRange,tRange, sRange,0, 0,0
//  	    };
        float texCoor[]=new float[]//顶点颜色值数组，每个顶点4个色彩值RGBA
                {
                        0,0, 0,1, 1,1,
                        1,1, 1,0, 0,0
                };
        //创建顶点纹理坐标数据缓冲
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = cbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoor);//向缓冲区中放入顶点着色数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================

    }
    /**
     * 初始化着色器
     * ① 加载顶点着色器与片元着色器脚本
     * ② 基于加载的着色器创建着色程序
     * ③ 根据着色程序获取 顶点属性引用 顶点颜色引用 总变换矩阵引用
     * @param mv
     */
    //初始化shader
    public void initShader(MyGLSurfaceView mv)
    {
        /*
         * mVertextShader是顶点着色器脚本代码
         * 调用工具类方法获取着色器脚本代码, 着色器脚本代码放在assets目录中
         * 传入的两个参数是 脚本名称 和 应用的资源
         * 应用资源Resources就是res目录下的那写文件
         */
        //加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        //基于顶点着色器与片元着色器创建程序
        mProgram = createProgram(mVertexShader, mFragmentShader);
        /*
         * 从着色程序中获取 属性变量 顶点坐标(颜色)数据的引用
         * 其中的"aPosition"是顶点着色器中的顶点位置信息
         * 其中的"aColor"是顶点着色器的颜色信息
         */
        //获取程序中顶点位置属性引用id
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //获取程序中顶点纹理坐标属性引用id
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //获取程序中总变换矩阵引用id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    /**
     * ① 设置绘制使用的着色程序
     * ② 初始化总变换矩阵
     * ③ 设置位移
     * ④ 设置旋转
     * ⑤ 应用最终变换矩阵
     * ⑥ 指定顶点与颜色位置缓冲数据
     * ⑦ 开始绘制
     */
    public void drawSelf(int texId)
    {
        //制定使用某套shader程序
        GLES20.glUseProgram(mProgram);
        //初始化变换矩阵
        Matrix.setIdentityM(mMMatrix, 0);
        //Matrix.setRotateM(mMMatrix,0,0,0,1,0);
        //设置沿Z轴正向位移1
        //Matrix.translateM(mMMatrix,0,0,0,1);
//         //设置绕y轴旋转
//         Matrix.rotateM(mMMatrix,0,yAngle,0,1,0);
//         //设置绕z轴旋转
//         Matrix.rotateM(mMMatrix,0,zAngle,0,0,1);
//         //设置绕x轴旋转
//         Matrix.rotateM(mMMatrix,0,xAngle,1,0,0);
        //将最终变换矩阵传入shader程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMMatrix), 0);
        //为画笔指定顶点位置数据
        GLES20.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3*4,
                        mVertexBuffer
                );
        //为画笔指定顶点纹理坐标数据
        GLES20.glVertexAttribPointer
                (
                        maTexCoorHandle,
                        2,
                        GLES20.GL_FLOAT,
                        false,
                        2*4,
                        mTexCoorBuffer
                );
        //允许顶点位置数据数组
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);

        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

        //绘制纹理矩形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

    public void setRatio(float ratio, float cordZ)
    {
        mRatio = ratio;
        mCordZ = cordZ;
    }
}
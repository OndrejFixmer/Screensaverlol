package educanet;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Square {

    private float x;
    private float y;
    private float z;

    public float[] vertices;

    private float[] colors = {
            0.6f, 1.0f, 1.0f, 1.0f,
            0.6f, 1.0f, 1.0f, 1.0f,
            0.6f, 1.0f, 1.0f, 1.0f,
            0.6f, 1.0f, 1.0f, 1.0f,
    };

    float[] textures = {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
    };

    private final int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    private int squareVaoId;
    private int squareVboId;
    private int squareEboId;
    private int squareColorId;
    private FloatBuffer cb;
    private static int uniformMatrixLocation;
    public Matrix4f matrix;
    public FloatBuffer matrixFloatBuffer;
    private static int textureIndicesId;
    private static int textureLoL;

    public Square(float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.z = size;
        float[] vertices = {
                x + size, y, 0.0f,
                x + size, y - size, 0.0f,
                x, y - size, 0.0f,
                x, y, 0.0f,
        };
        matrix = new Matrix4f()
                .identity();

        matrixFloatBuffer = BufferUtils.createFloatBuffer(16);

        this.vertices = vertices;
        cb = BufferUtils.createFloatBuffer(colors.length).put(colors).flip();

        squareVaoId = GL33.glGenVertexArrays();
        squareVboId = GL33.glGenBuffers();
        squareEboId = GL33.glGenBuffers();
        squareColorId = GL33.glGenBuffers();
        textureIndicesId = GL33.glGenBuffers();

        textureLoL = GL33.glGenTextures();
        obrazSwag();

        uniformMatrixLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");

        GL33.glBindVertexArray(squareVaoId);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
        FloatBuffer cb = BufferUtils.createFloatBuffer(colors.length)
                .put(colors)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);


        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);
        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);

        FloatBuffer tb = BufferUtils.createFloatBuffer(textures.length)
                .put(textures)
                .flip();


        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb, GL33.GL_STATIC_DRAW);
                GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
                GL33.glEnableVertexAttribArray(2);
                    GL33.glUseProgram(educanet.Shaders.shaderProgramId);

        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

        MemoryUtil.memFree(fb);
                MemoryUtil.memFree(tb);
                MemoryUtil.memFree(cb);
                    MemoryUtil.memFree(ib);
    }



    public void render() {
        GL33.glUseProgram(Shaders.shaderProgramId);

            matrix.get(matrixFloatBuffer);
                GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);
                    GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureLoL);
            GL33.glBindVertexArray(squareVaoId);
                    GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    private static float xSpeed = (float) 0.001;
    private static float ySpeed = (float) 0.001;

    public void update(long window) {
        matrix = matrix.translate(xSpeed, ySpeed, 0);
        x += xSpeed;
        y += ySpeed;

        if (x >= 0.75 || x <= -1) {
            if (x <= 0) {
            }
            if (x >= 0.1) {
            }
            xSpeed = -xSpeed;

        }
        if (y >= 1 || y <= -0.75) {
            ySpeed = -ySpeed;
        }


        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);
    }
    private static void obrazSwag() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer w = stack.mallocInt(1);
                    IntBuffer h = stack.mallocInt(1);
                        IntBuffer comp = stack.mallocInt(1);

            ByteBuffer obraz = STBImage.stbi_load("image/swag.png", w, h, comp, 3);
            if (obraz != null) {
                obraz.flip();

                GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureLoL);

                        GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGB, w.get(), h.get(), 0, GL33.GL_RGB, GL33.GL_UNSIGNED_BYTE, obraz);
                            GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);

                STBImage.stbi_image_free(obraz);
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
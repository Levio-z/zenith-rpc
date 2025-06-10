package com.zenith.zenithrpc.serializer;

import java.io.*;

/**
 * JDK 序列化器
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param object
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // 1、该函数用于将一个对象写入到ObjectOutputStream中，实现对象的序列化。
        // 2、通过调用writeObject方法，对象的实例数据和静态数据都会被写入，
        // 3、同时会保存对象之间的引用关系。在序列化过程中，对象的类信息也会被写入，
        // 4、以便在反序列化时能够恢复对象的状态。需要注意的是，要实现序列化，对象的类必须实现Serializable接口。
        objectOutputStream.writeObject(object);
        // 在这个示例中，objectOutputStream.close()方法不仅关闭ObjectOutputStream，还会关闭底层的ByteArrayOutputStream。
        // 这是因为ObjectOutputStream继承自FilterOutputStream，而FilterOutputStream的close()方法会调用其基础输出流（这里是ByteArrayOutputStream）的close()方法。
        // 因此，当你关闭ObjectOutputStream时，实际上也在关闭ByteArrayOutputStream。
        // 这样做是正确的，因为它确保了所有相关的资源都被正确释放，避免内存泄漏或资源浪费。
        // 同时，由于ByteArrayOutputStream是一个内存中的流，关闭它并不会像文件流那样删除或破坏数据，而是完成了序列化过程，使得toByteArray()能够返回序列化后的字节数组。
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            objectInputStream.close();
        }
    }
}
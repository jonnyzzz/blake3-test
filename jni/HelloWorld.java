class HelloWorld {
    private static native byte[] helloByte(byte[] input);

    static {
        System.loadLibrary("blake3_jni");
    }

    public static void main(String[] args) {
        byte[] outputByte = HelloWorld.helloByte("byte".getBytes());
        System.out.println(java.util.Arrays.toString(outputByte));
    }
}

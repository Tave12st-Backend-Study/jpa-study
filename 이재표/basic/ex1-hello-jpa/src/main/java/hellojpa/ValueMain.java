package hellojpa;

public class ValueMain {
    public static void main(String[] args) {
//        int a = 10;
//        int b = a;
//        a=20;

        Integer a = new Integer(10);
        Integer b = a;

        System.out.println("a = " + a);
        System.out.println("b = " + a);
    }
}

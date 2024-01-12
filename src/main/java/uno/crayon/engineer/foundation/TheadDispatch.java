package uno.crayon.engineer.foundation;

/**
 * @author
 */
public class TheadDispatch {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("asdfasdf");
        });
    }
}

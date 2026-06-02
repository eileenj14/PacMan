import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // create a JFrame (a window) with a particular text for title bar
        JFrame frame = new JFrame("Super Mario Game");

        // set frame to have 900 width, 983 height
        frame.setSize(900, 983);

        // auto-center frame on screen
        frame.setLocationRelativeTo(null);

        // apply setting so that clicking X in top right of window will close window and end
        // program
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create a DisplayPanel object
        DisplayPanel panel = new DisplayPanel();

        // add it to frame
        frame.add(panel);

        // call setVisible after everything else
        frame.setVisible(true);
    }
}

public class Polygon {
    public static void main(String[] args) {
        RobotModel robot = new RobotModel();
        RobotView view = new ScreenView();
        robot.setView(view);
        int n = 5, x = 1;
        if (args.length > 0) {
            n = Integer.parseInt(args[0]);
            if (args.length > 1) {
                x = Integer.parseInt(args[1]);
            }
        }
        for (int i = 0; i < n; i++) {
            robot.moveForward(100);
            robot.turnLeft(360.0 * x / n);
        }
    }
}

public class SnakeGame {
    private static GameFrame f;
    public static void main(String[] args) {
        newFrame(0);
    }
    public static void newFrame(int i){
        if(i!=0){
            f.dispose();
            GameFrame frame = new GameFrame();
            f = frame;
        }
        else
        {
            f = new GameFrame();
        }
    }
}

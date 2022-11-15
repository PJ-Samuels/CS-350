
public class Exp{

    public static double getExp(double lambda){
        double rand = Math.random();
        return Math.log(1-rand)/-(lambda);
        
    }
    public static void main(String[] args){
        double lambda= Double.valueOf(args[0]);
        int N = Integer.valueOf(args[1]);
        double result;
        for (int i = 0; i < N; i++) {
            result = getExp(lambda);
            System.out.println(result);
        }
    }
}

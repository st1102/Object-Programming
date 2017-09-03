import java.util.*;

public class SuperStacola {   
    abstract class Op {
        abstract public String opName();
        abstract public void exec();
    }
    class PrintOp extends Op {
        public String opName() { return "="; }
        public void exec() {
            for (double p : state.stack) {
                System.out.println(p);
            }
        }
    }
    abstract class BinOp extends Op {
        abstract public double op(double rand1, double rand2);
        public void exec() {
            double v2 = state.stack.pop();
            double v1 = state.stack.pop();
	    state.stack.push(op(v1, v2));
        }
	
    }

    static RobotModel robot = new RobotModel(); //RobotModelクラスのインスタンスrobotを作成
    abstract class OneOp extends Op { //Opを継承した新しい抽象クラスを作成
	abstract public void one_op(double rand1); //引数が１つの抽象メソッド、robotの動作を表すメソッドを呼び出す際に用いる
        public void exec() {
            double v1 = state.stack.pop(); //スタックから数値をポップ
	    one_op(v1); //その値の分だけ動作
        }
    }
    class ForwardOp extends OneOp { //基本１：前に進める
        public String opName() { return "進む"; }
	public void one_op(double rand1) { robot.moveForward(rand1);}
    }
    class RightOp extends OneOp { //基本２：右に回転させる
        public String opName() { return "右へ回る"; }
	public void one_op(double rand1) { robot.turnRight(rand1);}
    }
    class LeftOp extends OneOp { //基本３：左へ回転させる
        public String opName() { return "左へ回る"; }
        public void one_op(double rand1) { robot.turnLeft(rand1);}
    }
    class RepeatOp extends Op { //基本４：命令を繰り返す
        public String opName() { return "繰り返す"; }
        public void exec() {
	    double u1 = state.stack.pop(); //スタックから数値をポップ
	    for(int i = 0; i < u1; i++) { //その数値の分だけ繰り返し
		Program p = state.pstack.pop();
		state.pstack.push(p); //同じ命令を繰り返すため、一度ポップした命令をまたプッシュする
		p.run();
	    }
	    state.pstack.pop(); //最後に、繰り返した命令をポップ
        }
    }
    class ModOp extends BinOp { //発展１：余り
        public String opName() { return "余り"; }
        public double op(double rand1, double rand2) {
	    double mod = (long)rand1 % (long)rand2;
	    return mod;
	}
    }
    abstract class ProgOp extends Op { //引数として２つのプログラムをとる関数を実行する抽象クラス
	abstract public void op(Program prog1, Program prog2); //引数として２つのプログラムをとる関数
        public void exec() {
	    Program p2 = state.pstack.pop();
	    Program p1 = state.pstack.pop();
	    op(p1, p2);
	}
    }
    class IfOp extends ProgOp { //発展２：選ぶ
	public String opName() { return "選ぶ"; }
	public void op(Program prog1, Program prog2) {
	    double u1 = state.stack.pop();
	    if(u1 != 0){ //ポップした値が０でない
		prog1.run();
	    } else { //ポップした値が０
		prog2.run();
	    }
	}
    }
    abstract class VoidOp extends Op { //返り値がvoid型の関数を実行する抽象クラス
        abstract public void void_op(double rand1, double rand2); //返り値がvoid型の関数
        public void exec() {
            double v2 = state.stack.pop();
            double v1 = state.stack.pop();
	    void_op(v1, v2);
        }
	
    }
    class GtOp extends VoidOp { //発展３−１：より大きい
	public String opName() { return "より大きい"; }
        public void void_op(double rand1, double rand2) {
	    if(rand1 > rand2){ //先に積まれた値の方が大きいなら１をプッシュ
		state.stack.push(1.0);
	    } else {
		state.stack.push(0.0);
	    }
	}
    }
    class LtOp extends VoidOp { //発展３−２：より小さい
	public String opName() { return "より小さい"; }
        public void void_op(double rand1, double rand2) {
	    if(rand1 < rand2){ //先に積まれた方が値の方が小さいなら１をプッシュ
		state.stack.push(1.0);
	    } else {
		state.stack.push(0.0);
	    }
	}
    }
    class EqOp extends VoidOp { //発展３−３：等しい
	public String opName() { return "等しい"; }
        public void void_op(double rand1, double rand2) {
	    if(rand1 == rand2){ //値が小さければ１をプッシュ
		state.stack.push(1.0);
	    } else {
		state.stack.push(0.0);
	    }
	}
    }
    class DupOp extends OneOp { //発展４：コピー
	public String opName() { return "コピー"; }
        public void one_op(double rand1) {
	    state.stack.push(rand1); //ポップされた値を２度プッシュ
	    state.stack.push(rand1);
	}
    }
    class PopOp extends OneOp { //発展５：捨てる
	public String opName() { return "捨てる"; }
        public void one_op(double rand1) {
	    //値をポップして何もしない
	}
    }
    

    

    
    class AddOp extends BinOp {
        public String opName() { return "足す"; }
        public double op(double rand1, double rand2) { return rand1 + rand2; }
    }
    class SubOp extends BinOp {
        public String opName() { return "引く"; }
        public double op(double rand1, double rand2) { return rand1 - rand2; }
    }
    class MulOp extends BinOp {
        public String opName() { return "かける"; }
        public double op(double rand1, double rand2) { return rand1 * rand2; }
    }
    class DivOp extends BinOp {
        public String opName() { return "割る"; }
        public double op(double rand1, double rand2) { return rand1 / rand2; }
    }
    class NoOp extends Op {
        public String opName() { return "noop"; }
        public void exec() {
            // 何もしない
        }
    }
    class RunOp extends Op {
        public String opName() { return "実行する"; }
        public void exec() {
            state.pstack.pop().run();
        }
    }

    interface Token {
        abstract void interpret(); // トークンを解釈し，実行する
    }
    class Number implements Token {
        private double num;
        public Number(double num) { this.num = num; }
        public void interpret() {
            state.stack.push(num); // 数値を解釈すると，値スタックに積む
        }
        public String toString() { return Double.toString(num); }
    }
    class Name implements Token {
        private String name;
        public Name(String name) { this.name = name; }
        public void interpret() {
            if (state.ops.containsKey(name)) {
                state.ops.get(name).exec(); // 名前を解釈すると，命令辞書を引いて命令を実行する
            } else {
                System.out.println("エラー: 誤ったトークン: " + name);
            }
        }
        public String toString() { return name; }
    }
    class Program implements Token {
        private List<Token> prog;
        public Program(List<Token> prog) { this.prog = prog; }
        public void interpret() {
            state.pstack.push(this); // プログラムを解釈すると，自分をプログラムスタックに積む
        }
        List<Token> inspect() { return prog; }
        void run() {
            for (Token t : prog) {
                t.interpret();  // トークン列をすべて解釈する
            }
        }
        public String toString() {
            String result = "「 ";
            for (Token t : prog) { result += t.toString() + " "; }
            result += "」";
            return result;
        }
    }

    // 命令一覧表
    Op[] optable = {new AddOp(), new SubOp(), new MulOp(), new DivOp(), new PrintOp(), new RunOp(), new ForwardOp(), new RightOp(), new LeftOp(), new RepeatOp(), new ModOp(), new IfOp(), new GtOp(), new LtOp(), new EqOp(), new DupOp(), new PopOp()}; //今回作成した命令を追加した
    // 無視するトークン
    String[] noopTable = {"と", "を", "から", "で", "の", "回", "歩", "度"};

    // マシンの状態
    class State {
        Deque<Double> stack;    // 値スタック
        Deque<Program> pstack;  // プログラムスタック
        Map<String, Op> ops;    // 単語辞書
        State() {
            stack = new LinkedList<Double>();
            pstack = new LinkedList<Program>();
            ops = new HashMap<String, Op>();
        }
    }
    State state;
    Scanner scanner;
    SuperStacola() {	
        state = new State();
        scanner = new Scanner(System.in);
        // 辞書に命令語を登録する．
        for (Op op : optable) {
            state.ops.put(op.opName(), op);
        }
        Op noop = new NoOp();
        for (String n : noopTable) {
            state.ops.put(n, noop);
        }
    }
    Program parse(String endToken) {
        List<Token> list = new LinkedList<Token>();
        while (! scanner.hasNext(endToken)) {
            if (scanner.hasNext("「")) { // 次のトークンが"「”ならば
                scanner.next();     // "「"を読み飛ばす．
                Program subList = parse("」"); // "」"まで列にする
                list.add(subList);
            } else if (scanner.hasNextDouble()) {
                list.add(new Number(scanner.nextDouble()));
            } else {
                list.add(new Name(scanner.next()));
            }
        }
        scanner.next();     // endTokenを読み飛ばす．
        return new Program(list);
    }
    public void run() {
        while (scanner.hasNext()) {
            try {
                Program p = parse("。"); // "。"まで列にする
                System.out.println("入力: " + p.toString());
                p.run();   // 列を解釈する
            } catch (Exception e) {
                System.out.println("エラー");
                e.printStackTrace(System.out);
            }
        }
        System.out.println("終了。");
        System.exit(0);
    }
    
    public static void main(String[] args) {
	RobotView view = new ScreenView(); //ScreenViewクラスのインスタンスviewを作成、実行時は-cp oo:.とする
	robot.setView(view);
	new SuperStacola().run();
    }
}

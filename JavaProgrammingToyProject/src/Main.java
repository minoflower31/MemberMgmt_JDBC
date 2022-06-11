import service.MemberManager;

public class Main {

    public static void main(String[] args) {
        MemberManager manager = new MemberManager();

        System.out.println("#############################");
        System.out.println("### 회원 관리 프로그램 START ###");
        System.out.println("#############################");

        manager.readMenu();

        System.out.println("#############################");
        System.out.println("### 회원 관리 프로그램 END ###");
        System.out.println("#############################");
    }
}

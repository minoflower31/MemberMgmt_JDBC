package service;

import domain.Member;
import exception.InvalidInputMemberException;
import repository.MemberDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static service.CrudMode.*;

/* 개인적으로 검증을 추가해본 것
 * 1. 메뉴를 고를 때 0~4 이외에 숫자 혹은 문자, 문자를 포함한 숫자를 입력했을 경우
 * 2. 회원 가입, 회원 수정, 회원 삭제 시 성공 여부 (실패 case 추가)
 * 3. 회원 수정 및 삭제 시 공백일 경우
 */

public class MemberManager {

    private static final int SUCCESS_CNT = 1;

    private final BufferedReader br;
    private final MemberDAO dao;

    public MemberManager() {
        this.dao = new MemberDAO();
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void readMenu() {
        boolean isInputZero = false;
        String input;

        try {
            while (!isInputZero) {
                showDesc();
                input = br.readLine();
                if (isValidInputOnlyDigit(input))
                    continue;

                isInputZero = chooseMenu(Integer.parseInt(input));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidInputOnlyDigit(String s) {
        System.out.println();
        if (!s.matches("[0-9]+")) {
            System.out.println("입력 형식이 잘못됐습니다. 숫자만 입력해주세요.");
            return true;
        } else return false;
    }

    private boolean chooseMenu(int n) throws IOException {
        switch (n) {
            case 0: {
                return true;
            }
            case 1: {
                getMemberList();
                break;
            }
            case 2: {
                insertMember();
                break;
            }
            case 3: {
                updateMember();
                break;
            }
            case 4: {
                deleteMember();
                break;
            }
            default: {
                System.out.println("입력을 다시 시도해주세요.");
            }
        }
        return false;
    }

    private void getMemberList() {
        List<Member> findMembers = dao.select();
        if (findMembers.isEmpty()) {
            System.out.println("등록된 회원이 없습니다.");
        } else {
            for (Member m : findMembers) {
                System.out.println("---> " + m);
            }
        }
    }

    private void insertMember() throws IOException {
        Member member = new Member();

        try {
            member.setMemberId(inputMemberId(INSERT));
            member.setName(inputName());
            member.setPhoneNumber(inputPhoneNumber());
        } catch (IllegalStateException | InvalidInputMemberException e) {
            System.out.println(e.getMessage());
            return;
        }

        int resCnt = dao.insert(member);
        successOrFail(resCnt, "회원 가입");
    }

    private String inputMemberId(CrudMode mode) throws IOException, InvalidInputMemberException {
        System.out.print("아이디를 입력하세요 (형식 M-00001): ");
        String memberId = br.readLine();
        validateMemberIdNotBlankAndGrammar(memberId);
        existMemberId(memberId, mode);

        return memberId;
    }

    private void validateMemberIdNotBlankAndGrammar(String memberId) {
        if (memberId.isBlank())
            throw new InvalidInputMemberException("\n" + "아이디는 필수 입력 항목입니다.");

        if (!memberId.startsWith("M-") || memberId.length() != 7)
            throw new InvalidInputMemberException("\n" + "아이디는 'M-'으로 시작해야 하며, M-를 포함하여 7개의 문자로 구성해야 합니다.");
    }

    private void existMemberId(String memberId, CrudMode mode) {
        Member findMember = dao.selectById(memberId);

        if (mode.equals(INSERT)) {
            if (findMember != null)
                throw new IllegalStateException("\n" + findMember.getMemberId() + "가 이미 존재합니다.");
        } else {
            if (findMember == null)
                throw new IllegalStateException("\n" + memberId + " 회원 정보가 존재하지 않습니다.");
        }
    }

    private String inputName() throws IOException, InvalidInputMemberException {
        System.out.print("이름을 입력하세요: ");
        String name = br.readLine();
        validateName(name);

        return name;
    }

    private void validateName(String name) {
        if (name.isBlank())
            throw new InvalidInputMemberException("\n" + "이름은 필수 입력 항목입니다.");
    }

    private String inputPhoneNumber() throws IOException, InvalidInputMemberException {
        System.out.print("전화번호를 입력하세요: ");
        String phoneNumber = br.readLine();
        validatePhoneNumber(phoneNumber);

        return phoneNumber;
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.isBlank())
            throw new InvalidInputMemberException("\n" + "전화번호는 필수 입력 항목입니다.");

        if (!phoneNumber.matches("^\\d{3}-\\d{4}-\\d{4}$") || phoneNumber.length() != 13)
            throw new InvalidInputMemberException("\n" + "전화번호는 두 개의 '-'를 포함하여 총 13개의 문자로 구성해야 합니다");
    }

    private void updateMember() throws IOException {
        Member member = new Member();

        try {
            member.setMemberId(inputMemberId(UPDATE));
            member.setPhoneNumber(inputPhoneNumber());
        } catch (IllegalStateException | InvalidInputMemberException e) {
            System.out.println(e.getMessage());
            return;
        }

        int resCnt = dao.update(member);
        successOrFail(resCnt, "회원 수정");
    }

    private void deleteMember() throws IOException {
        String memberId;

        try {
            memberId = inputMemberId(DELETE);
        } catch (IllegalStateException | InvalidInputMemberException e) {
            System.out.println(e.getMessage());
            return;
        }

        int resCnt = dao.delete(memberId);
        successOrFail(resCnt, "회원 삭제");
    }

    private void successOrFail(int resCnt, String crudState) {
        if (resCnt == SUCCESS_CNT)
            System.out.println("\n" + "---> " + crudState + "에 성공하셨습니다.");
        else
            System.out.println("\n" + crudState + "에 실패하셨습니다. 다시 시도해주세요.");
    }

    private void showDesc() {
        System.out.println();
        System.out.println("목록을 원하시면 1번을 입력하세요.");
        System.out.println("등록을 원하시면 2번을 입력하세요.");
        System.out.println("수정을 원하시면 3번을 입력하세요.");
        System.out.println("삭제를 원하시면 4번을 입력하세요.");
        System.out.println("종료를 원하시면 0번을 입력하세요.");
        System.out.print("> ");
    }
}

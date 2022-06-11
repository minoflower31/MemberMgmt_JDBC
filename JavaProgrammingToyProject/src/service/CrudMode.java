package service;

enum CrudMode {
    //memberId를 입력받을 때 insert는 id가 중복됐는지에 대한 확인
    //나머지는 id가 없다면 Exception을 날리기 위한 상수

    SELECT, INSERT, DELETE, UPDATE;
}

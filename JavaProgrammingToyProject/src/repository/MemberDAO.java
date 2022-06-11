package repository;

import domain.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemberDAO {

    private static final String INSERT_SQL = "INSERT INTO member VALUES (?,?,?)";
    private static final String UPDATE_SQL = "UPDATE member SET member_id=?, phone_number=? WHERE member_id=?";
    private static final String DELETE_SQL = "DELETE FROM member WHERE member_id = ?";
    private static final String SELECT_SQL = "SELECT * FROM member";
    private static final String SELECT_FOR_ID_SQL = "SELECT * FROM member WHERE member_id = ?";

    public int insert(Member member) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(INSERT_SQL);
            stmt.setString(1, member.getMemberId());
            stmt.setString(2, member.getName());
            stmt.setString(3, member.getPhoneNumber());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(null, stmt, conn);
        }
        return 0;
    }

    public int update(Member member) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(UPDATE_SQL);
            stmt.setString(1, member.getMemberId());
            stmt.setString(2, member.getPhoneNumber());
            stmt.setString(3, member.getMemberId());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(null, stmt, conn);
        }
        return 0;
    }

    public int delete(String memberId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(DELETE_SQL);
            stmt.setString(1, memberId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(null, stmt, conn);
        }
        return 0;
    }

    public List<Member> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(SELECT_SQL);
            rs = stmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(getMemberThroughResultSet(rs));
            }
            return members;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, stmt, conn);
        }
        return Collections.emptyList();
    }

    public Member selectById(String memberId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.prepareStatement(SELECT_FOR_ID_SQL);
            stmt.setString(1, memberId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return getMemberThroughResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs, stmt, conn);
        }
        return null;
    }

    private Member getMemberThroughResultSet(ResultSet rs) throws SQLException {
        return new Member(
                rs.getString("member_id"),
                rs.getString("name"),
                rs.getString("phone_number")
        );
    }
}

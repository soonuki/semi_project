package com.cm.sale_share_board.dao;

import static com.cm.common.sql.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cm.sale_share_board.vo.SaleShareBoard;
import com.cm.sale_share_board.vo.SaleShareImage;
import com.cm.sale_share_board.vo.SaleShareLike;
import com.cm.sale_share_board.vo.SaleShareList;

public class SaleShareBoardDao {
   
   // 생성
   public int createBoard(SaleShareBoard ssb,SaleShareImage ssi ,Connection conn) {
      PreparedStatement pstmt = null;
      int result = 0;
      ResultSet rs = null;
      int postNo = 0;
      try {
         String sql = "INSERT INTO sale_share_post (board_type_id, local_gu_no, cate_code, user_no, post_title, post_text, prod_price,image_ori_name,image_new_name)"
               +"VALUES (2, (SELECT local_gu_no FROM location_gu WHERE local_gu_name = ?), ?, ?, ?, ?, ?,?,?)";
         
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, ssb.getLocal_gu_name());
         pstmt.setInt(2, ssb.getCate_code());
         pstmt.setInt(3, ssb.getUser_no());
         pstmt.setString(4, ssb.getPost_title());
         pstmt.setString(5, ssb.getPost_text());
         pstmt.setInt(6, ssb.getProd_price());
         pstmt.setString(7, ssi.getImage_ori_name());
         pstmt.setString(8,ssi.getImage_new_name());
         
         result = pstmt.executeUpdate();
         
         if(result>0) {
            String sql2 = "select post_no from `sale_share_post` where cate_code = ? and post_title = ? and post_text = ? and prod_price =?";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setInt(1, ssb.getCate_code());
            pstmt.setString(2, ssb.getPost_title());
            pstmt.setString(3, ssb.getPost_text());
            pstmt.setInt(4, ssb.getProd_price());
            rs = pstmt.executeQuery();   
         }
         
      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         close(pstmt);
      }
      return result;
   }
   
   // 리스트 (시간순) & 검색(제목)조회
   public List<SaleShareList> selectSaleBoardList(SaleShareList option, Connection conn){
      List<SaleShareList> list = new ArrayList<SaleShareList>();
      SaleShareList ssl = new SaleShareList();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
         String sql = "SELECT p.*, r.user_nick,r.user_no,"
                    + "  count(l.post_no) AS like_count, "
                    + " u.local_gu_name "
                    + "FROM `sale_share_post` p "
                    + "JOIN user r ON r.user_no = p.user_no "
                    + "LEFT JOIN sale_share_like l ON p.post_no = l.post_no "
                    + "LEFT JOIN `location_gu` u ON p.local_gu_no = u.local_gu_no "
                    + "WHERE p.post_release_yn = 'Y'";

         if (option.getPost_title() != null) {
             sql += " AND post_title LIKE CONCAT('%', '" + option.getPost_title() + "', '%')";
         }

         sql += "GROUP BY p.post_no, p.cate_code, u.local_gu_name, p.post_view, p.user_no, "
                 + "p.post_release_yn, p.deal_status, p.post_title, p.post_text, "
                 + "p.prod_price, p.prod_reg_date, p.prod_mod_date "
                 + " ORDER BY p.prod_reg_date DESC "
                 + " LIMIT " + option.getLimitPageNo() + ", " + option.getNumPerPage();
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         
         while(rs.next()) {
            ssl = new SaleShareList(rs.getString("image_new_name"),
                  rs.getInt("post_no"),
                  rs.getInt("user_no"),
                  rs.getString("user_nick"),
                  rs.getString("local_gu_name"),
                  rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                  rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                  rs.getString("post_title"),
                  rs.getString("post_text"),
                  rs.getInt("prod_price"),
                  rs.getInt("like_count"),
                  rs.getInt("deal_status"),
                  rs.getInt("cate_code"),
                  rs.getString("post_release_yn"),
                  rs.getInt("post_view"));
               list.add(ssl);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         close(rs);
         close(pstmt);
      }
      return list;
   }
   
   // 검색(내용) 조회
   public List<SaleShareList> selectSaleSearchText(SaleShareList option, Connection conn){
      List<SaleShareList> list = new ArrayList<SaleShareList>();
      SaleShareList ssl = new SaleShareList();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
         String sql =  "SELECT p.*, r.user_nick,r.user_no,"
                    + " count(l.post_no) AS like_count, "
                    + " u.local_gu_name, p.image_new_name "
                    + "FROM `sale_share_post` p " 
                    + "JOIN `location_gu` u ON p.local_gu_no = u.local_gu_no "
                    + " JOIN sale_share_like l ON p.post_no = l.post_no "
                    + "JOIN user r ON r.user_no = p.user_no "
                    + " WHERE post_release_yn= 'Y'";
         
         if(option.getPost_title() != null) {
            sql += " AND post_text LIKE CONCAT('%','"+option.getPost_title()+"','%')";
         }
         sql += "GROUP BY p.post_no, p.cate_code, u.local_gu_name, p.post_view, p.user_no, "
                 + "p.post_release_yn, p.deal_status, p.post_title, p.post_text, "
                 + "p.prod_price, p.prod_reg_date, p.prod_mod_date "
               + " ORDER BY p.prod_reg_date DESC"
            +" LIMIT "+option.getLimitPageNo()+", "+option.getNumPerPage();
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         
         while(rs.next()) {
            ssl = new SaleShareList(rs.getString("image_new_name"),
                  rs.getInt("post_no"),
                  rs.getInt("user_no"),
                  rs.getString("user_nick"),
                  rs.getString("local_gu_name"),
                  rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                  rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                  rs.getString("post_title"),
                  rs.getString("post_text"),
                  rs.getInt("prod_price"),
                  rs.getInt("like_count"),
                  rs.getInt("deal_status"),
                  rs.getInt("cate_code"),
                  rs.getString("post_release_yn"),
                  rs.getInt("post_view"));
               list.add(ssl);
         }
         
      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         close(rs);
         close(pstmt);
      }
      return list;
   }
   
   // 갯수
   public int selectSaleBoardCount(SaleShareList option,Connection conn) {
      int result = 0;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
         String sql = "select count(*) as cnt from sale_share_post where post_release_yn= 'Y' ";
         if(option.getPost_title() != null) {
            sql +=" and post_title like concat('%','"+option.getPost_title()+"','%')";
         }
         
         pstmt = conn.prepareStatement(sql);
         rs = pstmt.executeQuery();
         if(rs.next()) {
            result = rs.getInt("cnt");
         }
      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         close(rs);
         close(pstmt);
      }
      return result;
   }
   
   // 상세페이지
   public SaleShareList selectBoard(int postNo,Connection conn) {
      ResultSet rs = null;
      PreparedStatement pstmt = null;
      SaleShareList ssl = new SaleShareList();
      
      try {
         String sqlview = "UPDATE `sale_share_post` SET post_view = post_view + 1 WHERE post_no = ?";
         pstmt = conn.prepareStatement(sqlview);
         
          pstmt.setInt(1, postNo);
           int i = pstmt.executeUpdate();
         
           String sql2 = "SELECT p.*, u.local_gu_name, r.user_no, "
                   + "count(l.post_no) AS like_count, "
                   + "c.cate_code, p.post_release_yn, r.user_nick "
                   + "FROM sale_share_post p "
                   + "LEFT JOIN sale_share_like l ON p.post_no = l.post_no AND l.like_status = 1 "
                   + "JOIN prod_category c ON p.cate_code = c.cate_code "
                   + "JOIN user r ON r.user_no = p.user_no "
                   + "JOIN location_gu u ON p.local_gu_no = u.local_gu_no "
                   + "WHERE p.post_no = ? "
                   + "GROUP BY p.post_no, p.post_title, p.post_text, p.prod_price, p.prod_reg_date, "
                   + "p.prod_mod_date, p.deal_status, p.post_view, p.post_release_yn, "
                   + "c.cate_code, r.user_no, r.user_nick, u.local_gu_name";


         pstmt = conn.prepareStatement(sql2);
         pstmt.setInt(1, postNo);
         
         rs = pstmt.executeQuery();
         
         if(rs.next()) {
            ssl = new SaleShareList(rs.getString("image_new_name"),
                  rs.getInt("post_no"),
                  rs.getInt("user_no"),
                  rs.getString("user_nick"),
                  rs.getString("local_gu_name"),
                  rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                  rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                  rs.getString("post_title"),
                  rs.getString("post_text"),
                  rs.getInt("prod_price"),
                  rs.getInt("like_count"),
                  rs.getInt("deal_status"),
                  rs.getInt("cate_code"),
                  rs.getString("post_release_yn"),
                  rs.getInt("post_view"));
         }
         
      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         close(rs);
         close(pstmt);
      }
      return ssl;
   }
   
   // 수정
   public int editSale(String visiblity,int postNo,SaleShareBoard ssb, SaleShareImage ssi,Connection conn ) {
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      int result = 0;
      
      try {
         String sql = "update sale_share_post set cate_code = ?, post_title = ?, post_text = ?,"
               + "prod_price = ?, deal_status = ?, post_release_yn = ?, image_new_name = ? where post_no = ?";
         pstmt = conn.prepareStatement(sql);
         pstmt.setInt(1, ssb.getCate_code());
         pstmt.setString(2, ssb.getPost_title());
         pstmt.setString(3, ssb.getPost_text());
         pstmt.setInt(4, ssb.getProd_price());
         pstmt.setInt(5, ssb.getDeal_status());
         pstmt.setString(6, visiblity);
         pstmt.setString(7, ssi.getImage_new_name());
         pstmt.setInt(8, postNo);
      
         
         result = pstmt.executeUpdate();
         
      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         close(rs);
         close(pstmt);
      }
      return result;
   }
   
   // 삭제
   public int deleteSale(int postNo, Connection conn) {
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      int result = 0;
      
      try {
            String sql2 = "delete from sale_share_post where post_no = ?";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setInt(1, postNo);
            
             result = pstmt.executeUpdate();
         
      }catch(Exception e) {
         e.printStackTrace();
      }finally {
         close(rs);
         close(pstmt);
      }
      return result;
   }
   
   // 나눔 리스트
      public List<SaleShareList> selectShare(SaleShareList option,Connection conn) {
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         List<SaleShareList> list = new ArrayList<SaleShareList>();
         SaleShareList ssl = null;
         
         try {
            String sql = "SELECT p.*, u.local_gu_name, count(l.post_no) AS like_count,r.user_no, r.user_nick "
                       + "FROM sale_share_post p "
                       + "JOIN user r ON r.user_no = p.user_no "
                       + "JOIN location_gu u ON p.local_gu_no = u.local_gu_no "
                       + "LEFT JOIN sale_share_like l ON p.post_no = l.post_no AND l.like_status = 1 "
                       + "WHERE p.prod_price = 0 AND p.post_release_yn = 'Y' "
                       + "GROUP BY p.post_no "
                       + "ORDER BY p.prod_reg_date DESC "
                       + "LIMIT " + option.getLimitPageNo() + ", " + option.getNumPerPage();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
               ssl = new SaleShareList(rs.getString("image_new_name"),
                     rs.getInt("post_no"),
                     rs.getInt("user_no"),
                     rs.getString("user_nick"),
                     rs.getString("local_gu_name"),
                     rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                     rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                     rs.getString("post_title"),
                     rs.getString("post_text"),
                     rs.getInt("prod_price"),
                     rs.getInt("like_count"),
                     rs.getInt("deal_status"),
                     rs.getInt("cate_code"),
                     rs.getString("post_release_yn"),
                     rs.getInt("post_view"));
                  list.add(ssl);
               }
            }catch(Exception e) {
               e.printStackTrace();
            }finally {
               close(rs);
               close(pstmt);
            }
            return list;
         }
      
       // 나눔리스트 - 카운트
      public int selectBoardShareCount(SaleShareList option,Connection conn) {
         int result = 0;
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         
         try {
            String sql = "select count(*) as cnt from sale_share_post where prod_price = 0 and post_release_yn = 'Y'";            
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if(rs.next()) {
               result = rs.getInt("cnt");
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return result;
      }

      // 판매리스트
      public List<SaleShareList> selectSale(SaleShareList option,Connection conn) {
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         List<SaleShareList> list = new ArrayList<SaleShareList>();
         SaleShareList ssl = null;
         
         try {
            String sql = "SELECT p.*, u.local_gu_name, count(l.post_no) AS like_count, r.user_no, r.user_nick "
                       + "FROM sale_share_post p "
                       + "JOIN user r ON r.user_no = p.user_no "
                       + "JOIN location_gu u ON p.local_gu_no = u.local_gu_no "
                       + "LEFT JOIN sale_share_like l ON p.post_no = l.post_no AND l.like_status = 1 "
                       + "WHERE p.prod_price > 0 AND p.post_release_yn = 'Y' "
                       + "GROUP BY p.post_no "
                       + "ORDER BY p.prod_reg_date DESC "
                       + "LIMIT " + option.getLimitPageNo() + ", " + option.getNumPerPage();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
               ssl = new SaleShareList(rs.getString("image_new_name"),
                     rs.getInt("post_no"),
                     rs.getInt("user_no"),
                     rs.getString("user_nick"),
                     rs.getString("local_gu_name"),
                     rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                     rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                     rs.getString("post_title"),
                     rs.getString("post_text"),
                     rs.getInt("prod_price"),
                     rs.getInt("like_count"),
                     rs.getInt("deal_status"),
                     rs.getInt("cate_code"),
                     rs.getString("post_release_yn"),
                     rs.getInt("post_view"));
                  list.add(ssl);
               }
            }catch(Exception e) {
               e.printStackTrace();
            }finally {
               close(rs);
               close(pstmt);
            }
            return list;
         }
       // 페이지카운트 - 판매
      public int selectBoardSellCount(SaleShareList option,Connection conn) {
         int result = 0;
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         
         try {
            String sql = "select count(*) as cnt from sale_share_post where prod_price > 0 and post_release_yn = 'Y'";            
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if(rs.next()) {
               result = rs.getInt("cnt");
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return result;
      }
      
      // 끌어올리기
      public int salePull(int postNo, Connection conn) {
         int result = 0;
         PreparedStatement pstmt = null;
         
         try {
            String sql = "UPDATE sale_share_post SET prod_reg_date = NOW() WHERE post_no=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postNo);
            
            result = pstmt.executeUpdate();
                  
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(pstmt);
         }
         return result;
      }
      
      // 좋아요 수
      public int saleLike(SaleShareLike like, Connection conn) {
           int result = 0;
            int status = 1;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                String sql = "SELECT * FROM `sale_share_like` WHERE post_no=? AND like_user_no=?";
                 pstmt = conn.prepareStatement(sql);
                 pstmt.setInt(1, like.getPost_no());
                 pstmt.setInt(2, like.getLike_user_no());
                 rs = pstmt.executeQuery();
                 
                 if (rs.next()) {
                     status = rs.getInt("like_status");
                     if (status == 1) {
                         status = 0;
                     } else {
                         status = 1;
                     }
                     rs.close();
                     pstmt.close();

                     
                     String sql1 = "UPDATE `sale_share_like` SET like_status=? WHERE post_no=? AND like_user_no=?";
                     pstmt = conn.prepareStatement(sql1);
                     pstmt.setInt(1, status);
                     pstmt.setInt(2, like.getPost_no());
                     pstmt.setInt(3, like.getLike_user_no());
                     result = pstmt.executeUpdate();
                     
                     rs.close();
                     pstmt.close();

                     
                 } else {
                     
                     String sql2 = "INSERT INTO `sale_share_like` (post_no, like_user_no, like_status) VALUES (?,?,?)";
                     pstmt = conn.prepareStatement(sql2);
                     pstmt.setInt(1, like.getPost_no());
                     pstmt.setInt(2, like.getLike_user_no());
                     pstmt.setInt(3, status);
                     result = pstmt.executeUpdate();
                 }
               
               
            } catch (Exception e) {
               e.printStackTrace();
            } finally {
               close(rs);
               close(pstmt);
            }
            return result;
      }
      
      // 정렬 - 가격 낮은 순
      public List<SaleShareList> selectArrayPrice(Connection conn) {
         ResultSet rs = null;
         SaleShareList ssl = new SaleShareList();
         List<SaleShareList> list = new ArrayList<SaleShareList>();
         PreparedStatement pstmt = null;
         try {
            String sql = "SELECT p.*,COALESCE(l.like_count, 0) AS like_count, u.user_nick, l.local_gu_name "
                     +" FROM sale_share_post p"
                     +" JOIN location_gu l ON l.local_gu_no = p.local_gu_no "
                     +" JOIN user u ON u.user_no = p.user_no "
                     +" LEFT JOIN ("
                      +"SELECT post_no, COUNT(*) AS like_count"
                      +" FROM sale_share_like "
                      +"GROUP BY post_no "
                      +") l ON p.post_no = l.post_no "
                      +"ORDER BY p.prod_price ASC";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
               ssl = new SaleShareList(rs.getString("image_new_name"),
                     rs.getInt("post_no"),
                     rs.getInt("user_no"),
                     rs.getString("user_nick"),
                     rs.getString("local_gu_name"),
                     rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                     rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                     rs.getString("post_title"),
                     rs.getString("post_text"),
                     rs.getInt("prod_price"),
                     rs.getInt("like_count"),
                     rs.getInt("deal_status"),
                     rs.getInt("cate_code"),
                     rs.getString("post_release_yn"),
                     rs.getInt("post_view"));
                  list.add(ssl);
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return list;
      }
      
      // 정렬 - 조회순
      public List<SaleShareList> selectArrayView(Connection conn){
         ResultSet rs = null;
         SaleShareList ssl = new SaleShareList();
         List<SaleShareList> list = new ArrayList<SaleShareList>();
         PreparedStatement pstmt = null;
         try {
            String sql = "SELECT p.*, COALESCE(l.like_count, 0) AS like_count, u.user_nick,l.local_gu_name "
                  +" FROM sale_share_post p"
                  +" JOIN location_gu l ON l.local_gu_no = p.local_gu_no "
                  + " JOIN user u ON u.user_no = p.user_no "
                  +" LEFT JOIN ("
                   +"SELECT post_no, COUNT(*) AS like_count"
                   +" FROM sale_share_like "
                   +"GROUP BY post_no "
                   +") l ON p.post_no = l.post_no "
                   +"ORDER BY p.post_view DESC";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
               ssl = new SaleShareList(rs.getString("image_new_name"),
                     rs.getInt("post_no"),
                     rs.getInt("user_no"),
                     rs.getString("user_nick"),
                     rs.getString("local_gu_name"),
                     rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                     rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                     rs.getString("post_title"),
                     rs.getString("post_text"),
                     rs.getInt("prod_price"),
                     rs.getInt("like_count"),
                     rs.getInt("deal_status"),
                     rs.getInt("cate_code"),
                     rs.getString("post_release_yn"),
                     rs.getInt("post_view"));
                  list.add(ssl);
            }
            
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return list;
      }
      
      // 정렬 - 좋아요 순
      public List<SaleShareList> selectArrayHeart(Connection conn){
         ResultSet rs = null;
         SaleShareList ssl = new SaleShareList();
         List<SaleShareList> list = new ArrayList<SaleShareList>();
         PreparedStatement pstmt = null;
         try {
            String sql = "SELECT p.*, COALESCE(l.like_count, 0) AS like_count, u.user_nick, l.local_gu_name "
                  +"FROM sale_share_post p"
                  +" JOIN user u ON u.user_no = p.user_no"
                  +" JOIN location_gu l ON l.local_gu_no = p.local_gu_no "
                  +"LEFT JOIN ("
                      +"SELECT post_no, COUNT(*) AS like_count "
                      +"FROM sale_share_like "
                      +"GROUP BY post_no "
                  +") l ON p.post_no = l.post_no "
                  +"ORDER BY COALESCE(l.like_count, 0) DESC";
            pstmt = conn.prepareStatement(sql);
            
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
               ssl = new SaleShareList(rs.getString("image_new_name"),
                     rs.getInt("post_no"),
                     rs.getInt("user_no"),
                     rs.getString("user_nick"),
                     rs.getString("local_gu_name"),
                     rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                     rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                     rs.getString("post_title"),
                     rs.getString("post_text"),
                     rs.getInt("prod_price"),
                     rs.getInt("like_count"),
                     rs.getInt("deal_status"),
                     rs.getInt("cate_code"),
                     rs.getString("post_release_yn"),
                     rs.getInt("post_view"));
                  list.add(ssl);
            }
            
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return list;
      }
      
      // 내 글 카운트 
      public int mypageSellCount(SaleShareList option,int userNo,Connection conn) {
         int result = 0;
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         
         try {
            String sql = "select count(*) as cnt from sale_share_post where user_no = ?";            
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userNo);
            rs = pstmt.executeQuery();
            if(rs.next()) {
               result = rs.getInt("cnt");
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return result;
      }
      
      
      
      public List<SaleShareList> myPageList(int userNo, Connection conn){
         PreparedStatement pstmt = null;
         List<SaleShareList> list = new ArrayList<SaleShareList>();
         SaleShareList ssl = new SaleShareList();
         ResultSet rs = null;
         try {
            String sql = "SELECT p.*,  COALESCE(l.like_count, 0) AS like_count, u.user_nick, g.local_gu_name "
                       +"FROM sale_share_post p JOIN " 
                       +"location_gu g ON g.local_gu_no = p.local_gu_no JOIN " 
                       +"user u ON u.user_no = p.user_no "
                       +"LEFT JOIN ("
                       +"SELECT post_no, COUNT(*) AS like_count FROM " 
                       +"sale_share_like "
                       +"GROUP BY post_no "
                       +") l ON p.post_no = l.post_no "
                       +"WHERE p.user_no = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userNo);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
               ssl = new SaleShareList(rs.getString("image_new_name"),
                     rs.getInt("post_no"),
                     rs.getInt("user_no"),
                     rs.getString("user_nick"),
                     rs.getString("local_gu_name"),
                     rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                     rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                     rs.getString("post_title"),
                     rs.getString("post_text"),
                     rs.getInt("prod_price"),
                     rs.getInt("like_count"),
                     rs.getInt("deal_status"),
                     rs.getInt("cate_code"),
                     rs.getString("post_release_yn"),
                     rs.getInt("post_view"));
                  list.add(ssl);
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return list;
      }
      
      // 마이페이지 - 내가 좋아요 한 글
      public List<SaleShareList> myPageLkieList(int userNo, Connection conn){
         PreparedStatement pstmt = null;
         List<SaleShareList> list = new ArrayList<SaleShareList>();
         SaleShareList ssl = new SaleShareList();
         ResultSet rs = null;
         try {
            String sql = "SELECT p.*, COALESCE(l.like_count, 0) AS like_count, u.user_nick, g.local_gu_name "
                  + "FROM sale_share_post p "
                  + "JOIN location_gu g ON g.local_gu_no = p.local_gu_no "
                  + "JOIN user u ON u.user_no = p.user_no "
                  + "LEFT JOIN ("
                  + "    SELECT post_no, COUNT(*) AS like_count "
                  + "    FROM sale_share_like "
                  + "    GROUP BY post_no "
                  + ") l ON p.post_no = l.post_no "
                  + "WHERE p.post_no IN ("
                  + "    SELECT post_no "
                  + "    FROM sale_share_like "
                  + "    WHERE like_user_no = ? AND like_status = 1)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userNo);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
               ssl = new SaleShareList(rs.getString("image_new_name"),
                     rs.getInt("post_no"),
                     rs.getInt("user_no"),
                     rs.getString("user_nick"),
                     rs.getString("local_gu_name"),
                     rs.getTimestamp("prod_reg_date").toLocalDateTime(),
                     rs.getTimestamp("prod_mod_date").toLocalDateTime(),
                     rs.getString("post_title"),
                     rs.getString("post_text"),
                     rs.getInt("prod_price"),
                     rs.getInt("like_count"),
                     rs.getInt("deal_status"),
                     rs.getInt("cate_code"),
                     rs.getString("post_release_yn"),
                     rs.getInt("post_view"));
                  list.add(ssl);
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return list;
      }
      
      // 내 마이페이지 좋아요 수 카운트
      public int myLikeCount(SaleShareList option,int userNo,Connection conn) {
         int result = 0;
         PreparedStatement pstmt = null;
         ResultSet rs = null;
         
         try {
            String sql = "select count(*) as cnt from sale_share_like where like_user_no = ? and like_status = 1";            
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userNo);
            rs = pstmt.executeQuery();
            if(rs.next()) {
               result = rs.getInt("cnt");
            }
         }catch(Exception e) {
            e.printStackTrace();
         }finally {
            close(rs);
            close(pstmt);
         }
         return result;
      }
      
   }


 
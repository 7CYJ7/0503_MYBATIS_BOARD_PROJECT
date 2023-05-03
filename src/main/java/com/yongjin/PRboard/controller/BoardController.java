package com.yongjin.PRboard.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yongjin.PRboard.dao.IDao;
import com.yongjin.PRboard.dto.FbMemberDto;
import com.yongjin.PRboard.dto.FreeBoardDto;

@Controller
public class BoardController {
	
	@Autowired
	private SqlSession sqlSession;
	
	@RequestMapping(value = "/joinMember")
	public String joinMember() {
		return "joinMember";		
	}
	
	@RequestMapping(value = "/joinOk", method = RequestMethod.POST)
	public String joinOk(HttpServletRequest request, Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		int checkId = dao.checkIdDao(request.getParameter("mid"));
		
		if(checkId == 0) {		
			dao.joinMemberDao(request.getParameter("mid"), request.getParameter("mpw"), request.getParameter("mname"), request.getParameter("memail"));
			model.addAttribute("memberName", request.getParameter("mname"));
			model.addAttribute("checkIdFlag", "joinOk");
		} else {
			model.addAttribute("checkIdFlag", "1");
		}
		
		
		
		return "joinOk";
	}
	
	@RequestMapping(value = "/checkId")
	public String checkId(HttpServletRequest request, Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		int checkIdFlag = dao.checkIdDao(request.getParameter("checkId"));
		 //이미 가입된 회원이면 1, 아니면 0이 반환
		
		model.addAttribute("checkIdFlag", checkIdFlag);
		
		return "joinOk";
		
	}
	
	@RequestMapping(value = "/login")
	public String login() {
		return "login";		
	}
	
	@RequestMapping(value = "/loginOk", method = RequestMethod.POST)
	public String loginOk(HttpServletRequest request, Model model, HttpSession session) {
		
		String mid = request.getParameter("mid");
		String mpw = request.getParameter("mpw");
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		int checkIdFlag = dao.checkIdDao(mid);
		
		model.addAttribute("checkIdFlag", checkIdFlag);
		
		int checkIdPwFlag = dao.checkIdPwDao(mid, mpw);
		
		model.addAttribute("checkIdPwFlag", checkIdPwFlag);		
		
		
		if(checkIdPwFlag == 1) { //로그인 성공
//			HttpSession session = request.getSession();//컨트롤러에서 세션객체 가져오기
			session.setAttribute("sessionId", mid);
			model.addAttribute("memberId", mid);
		}
		
		return "loginOk";
		
	}
	
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		
		session.invalidate();//모든 세션 삭제
		
		return "login";
	}
	
	@RequestMapping(value = "write_form")
	public String write_form(HttpSession session, Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		String sid = (String)session.getAttribute("sessionId");
		
		if(sid == null) {
			return "redirect:login";
		} else {
		
			FbMemberDto dto = dao.getMemberInfo(sid);
		
			model.addAttribute("memberDto", dto);
		
			return "writeForm";
		}
	}
	
	@RequestMapping(value = "/write")
	public String write(HttpServletRequest request) {
		
		String fid = request.getParameter("mid");
		String fname = request.getParameter("mname");
		String ftitle = request.getParameter("ftitle");
		String fcontent = request.getParameter("fcontent");
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		dao.writeDao(fid, fname, ftitle, fcontent);
		
		return "redirect:list";		
	}
	
	@RequestMapping(value = "/list")
	public String list(Model model) {
		
		IDao dao = sqlSession.getMapper(IDao.class);
		
		ArrayList<FreeBoardDto> dtos = dao.listDao();
		
		model.addAttribute("list", dtos);
		
		return "list";
	}
	
	
}

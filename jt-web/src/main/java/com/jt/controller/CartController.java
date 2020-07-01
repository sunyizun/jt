package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserTheadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Reference(timeout = 3000,check = false)
	private DubboCartService cartService;
	/**
	 * 页面取值items="${cartList}"
	 * @param model
	 * @return
	 */
	@RequestMapping("/show")
	public String findCartList(Model model) {
		//Long userId =7L;
		//User user =(User) request.getAttribute("JT_USER");
		//Long userId=user.getId();
		Long userId=UserTheadLocal.get().getId();
		List<Cart> cartList =cartService.findCartListByUserId(userId);
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	//购物车数量修改
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateCartNum(Cart cart) {
		try {
			//Long userId = 7L;
			Long userId=UserTheadLocal.get().getId();
			cart.setUserId(userId);
			cartService.updateCartNum(cart);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	
	}
	/**
	 * 删除购物车
	 * delete/562379
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCartNum(Cart cart) {
		//Long userId = 7L;
		Long userId=UserTheadLocal.get().getId();
		cart.setUserId(userId);
		cartService.deleteCart(cart);
		//重定向到购物车页面
		return "redirect:/cart/show.html";
	}
	/**
	 * 新增购物车
	 */
	@RequestMapping("/add/{itemId}")
	public String insertCartNum(Cart cart) {
		//Long userId = 7L;
		Long userId=UserTheadLocal.get().getId();
		cart.setUserId(userId);
		cartService.insertCart(cart);
		//重定向到购物车页面
		return "redirect:/cart/show.html";
	}
	
	
	
	
	
	
}

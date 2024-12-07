package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.Address;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String getCreateUser(ModelMap model) {

		model.put("user", new User());

		return "register";
	}

	@PostMapping("/register")
	public String postCreateUser(User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/register";
	}

	@GetMapping("/users")
	public String getAllUsers(ModelMap model) {
		Set<User> users = userService.findAll();

		model.put("users", users);
		if (users.size() == 1) {
			User singleUser = users.iterator().next();
			model.put("user", singleUser);

			Address address = singleUser.getAddress();
			if (address == null) {
				address = new Address();
			}
			model.put("address", address);
		} else if (users.isEmpty()) {
			model.put("message", "No users available.");
		}
		return "users";
	}

	@GetMapping("/users/{userId}")
	public String getOneUser(ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		if (user == null) {
			model.put("error", "User not found.");
			return "error";
		}
		model.put("users", List.of(user));
		model.put("user", user);

		Address address = user.getAddress();
		if (address == null) {
			address = new Address();
		}
		model.put("address", address);

		List<Account> accounts = user.getAccounts();
		model.put("accounts", accounts);

		return "users";
	}

	@PostMapping("/users/{userId}")
	public String postOneUser(@PathVariable Long userId, @ModelAttribute User user, @ModelAttribute Address address) {
		User existingUser = userService.findById(userId);
		user.setAccounts(existingUser.getAccounts());

		if (address != null) {
			address.setUser(user);
			user.setAddress(address);
		} else {
			user.setAddress(existingUser.getAddress());
		}

		userService.saveUser(user);
		return "redirect:/users/" + user.getUserId();
	}

	@PostMapping("/users/{userId}/delete")
	public String deleteOneUser(@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}

	@PostMapping("/users/{userId}/accounts")
	public String postNewAccount(@PathVariable Long userId, @ModelAttribute Account account) {
		if (account.getAccountId() != null) {
			account.setAccountId(null);
		}

		User user = userService.findById(userId);
		if (account.getAccountId() == null) {
			account.setUsers(Collections.singletonList(user));
			user.getAccounts().add(account);
		}

		Account newAccount = userService.saveAccount(account);

		return "redirect:/users/" + userId + "/accounts/" + newAccount.getAccountId();
	}

	@GetMapping("/users/{userId}/accounts/{accountId}")
	public String getOneAccount(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
		User user = userService.findById(userId);
		model.put("user", user);

		Account account = userService.findAccountById(accountId);
		model.put("account", account);

		return "account";
	}

	@PostMapping("/users/{userId}/accounts/{accountId}")
	public String postOneAccount(@PathVariable Long accountId, @PathVariable Long userId, @ModelAttribute Account account) {
		User user = userService.findById(userId);
		Account existingAccount = userService.findAccountById(accountId);
		existingAccount.setAccountName(account.getAccountName());

		userService.saveAccount(existingAccount);
		return "redirect:/users/" + userId;
	}
}
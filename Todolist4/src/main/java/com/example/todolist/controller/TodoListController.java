package com.example.todolist.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService;
	private final HttpSession session;

	// Todo一覧表示 A: Todolist
	@GetMapping("/todo")
	public ModelAndView showTodoList(ModelAndView mv) {
		// 一覧を検索して表示する
		mv.setViewName("todoList");
		List<Todo> todoList = todoRepository.findAll();
		mv.addObject("todoList", todoList);
		mv.addObject("todoQuery", new TodoQuery()); //Todolist4で追加
		return mv;
	}

	// Todo入力フォーム表示 A: Todolist2 E: Todolist3
	// 【処理１】Todo一覧画面 (todoList.html) で　"新規追加"　リンクがクリックされると
	@PostMapping("/todo/create/form")
	public ModelAndView createTodo(ModelAndView mv) {
		mv.setViewName("todoForm");
		mv.addObject("todoData", new TodoData());
		session.setAttribute("mode", "create");
		return mv;
	}
	
	// idごとのページを表示 A: Todolist3
	// 一覧画面でタイトルがクリックされると
	@GetMapping("/todo/{id}")
	public ModelAndView todoById(@PathVariable(name= "id") int id, ModelAndView mv) {
		mv.setViewName("todoForm");
		Todo todo = todoRepository.findById(id).get();
		mv.addObject("todoData", todo);
		session.setAttribute("mode", "update");
		return mv;
	}

	// Todo追加処理 A: Todolist2 E: Todolist3
	//【処理２】Todo 入力画面 (todoForm.html) で "登録"ボタンがクリックされると
	@PostMapping("/todo/create/do")
	public String createTodo(@ModelAttribute @Validated TodoData todoData, BindingResult result, Model model) {
		//エラーチェック
		boolean isValid = todoService.isValid(todoData, result);
		if(!result.hasErrors() && isValid) {
			//エラーなしの時の処理
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";
		} else {
			//エラーがある時の処理
			// model.addAttribute("todoData", todoData);
			return "todoForm";
		}
	}

	// Todo一覧へ戻る A: Todolist2
	// 【処理３】Todo入力画面で"キャンセル登録"ボタンがクリックされると
	@PostMapping("/todo/cancel")
	public String cancel() {
		return "redirect:/todo";
	}
	
	// レコードを更新する A:todolist3
	// 更新画面で"更新"ボタンをクリックすると
	@PostMapping("/todo/update")
	public String updateTodo(@ModelAttribute @Validated TodoData todoData, BindingResult result, Model model) {
		//エラーチェック
		boolean isValid = todoService.isValid(todoData, result);
		if(!result.hasErrors() && isValid) {
			//エラーなしの処理
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return "redirect:/todo";
		} else {
			//エラーがある時の処理
			// model.addAttribute("todoData", todoData);
			return "todoForm";
		}
	}
	
	// レコードを削除する A: todolist3
	// 更新画面で"削除"ボタンをクリックすると
	@PostMapping("/todo/delete")
	public String deleteTodo(@ModelAttribute TodoData todoData) {
		todoRepository.deleteById(todoData.getId());
		return "redirect:/todo";
	}
	
	// レコードを検索する A: todolist4
	// 検索ボタンが押されたときの処理
	@PostMapping("/todo/query")
	public ModelAndView queryTodo(@ModelAttribute TodoQuery todoQuery, BindingResult result, ModelAndView mv) {
		mv.setViewName("todoList");

		List<Todo> todoList = null;
		if (todoService.isValid(todoQuery, result)) {
			// エラーがなければ検索
			todoList = todoService.doQuery(todoQuery);
		}
		// mv.addObject("todoQuery", todoQuery);
		mv.addObject("todoList", todoList);

		return mv;
	}
}

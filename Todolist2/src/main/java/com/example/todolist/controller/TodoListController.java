package com.example.todolist.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService;

	// Todo一覧表示 A: Todolist
	@GetMapping("/todo")
	public ModelAndView showTodoList(ModelAndView mv) {
		// 一覧を検索して表示する
		mv.setViewName("todoList");
		List<Todo> todoList = todoRepository.findAll();
		mv.addObject("todoList", todoList);
		return mv;
	}

	// Todo入力フォーム表示 A: Todolist2
	// 【処理１】Todo一覧画面 (todoList.html) で　"新規追加"　リンクがクリックされると
	@GetMapping("/todo/create")
	public ModelAndView createTodo(ModelAndView mv) {
		mv.setViewName("todoForm");
		mv.addObject("todoData", new TodoData());
		return mv;
	}

	// Todo追加処理 A: Todolist2
	//【処理２】Todo 入力画面 (todoForm.html) で "登録"ボタンがクリックされると
	@PostMapping("/todo/create")
	public ModelAndView createTodo(@ModelAttribute @Validated TodoData todoData, BindingResult result, ModelAndView mv) {
		//エラーチェック
		boolean isValid = todoService.isValid(todoData, result);
		if(!result.hasErrors() && isValid) {
			//エラーなしの時の処理
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush(todo);
			return showTodoList(mv);
		} else {
			//エラーがある時の処理
			mv.setViewName("todoForm");
			// mv.addObject("todoData", todoData); ←@ModelAttributeが付与されたオブジェクトは遷移先で使用できるため、省略可
			return mv;
		}
	}

	// Todo一覧へ戻る A: Todolist2
	// 【処理３】Todo入力画面で"キャンセル登録"ボタンがクリックされると
	@PostMapping("/todo/cancel")
	public String cancel() {
		return "redirect:/todo";
	}
}

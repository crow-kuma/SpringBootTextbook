package com.example.todolist.service;

@Service
public class TodoService {
	public boolean isValid(TodoData TodoData, BindingResult result) {
		boolean ans = true;

		// 件名が全角スペースだけで構成されていたらエラー
		String title = todoData.getTitle();
		if(title != null && !title.equals("")) {
			boolean isAllDoubleSpace = true;
			for(int i = 0; i < title.length(); i++) {
				if(title.charAt(i) != '　') {
					isAllDoubleSpace =false;
					break;
				}
			}
			if(isAllDoubleSpace) {
				FieldError fieldError = new FieldError(
					result.getObjectName(),
					"title",
					"件名が全角スペースです");
				result.addError(fieldError);
				ans = false;
			}
		}

		// 期限が過去日付ならエラー
		String deadline = todoData.getDeadline();
		if(!deadline.equals("")) {
			LocalDate today = LocalDate.now();
			LocalDate deadlineDate = null;
			try {
				deadlineDate = LocalDate.parse(deadline);
				if (deadlineDate.isBefore(today)) {
					FieldError fieldError = new FieldError (
						result.getObjectName(),
						"deadline",
						"期限を設定する場合は、今日以降の期限を設定してください");
					result.addError(FieldError);
					ans = false;
				}
			} catch (DateTimeException e) {
				FieldError fieldError = new FieldError (
					result.getObjectName(),
					"deadline",
					"期限を設定する場合は、yyyy-mm-dd形式で入力してください");
				result.addError(fieldError);
				ans = false;
			}
		}
		return ans;
	}
}

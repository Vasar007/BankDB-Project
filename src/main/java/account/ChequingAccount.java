package account;


class ChequingAccount extends AccountImpl {
	ChequingAccount(String id, boolean flag){
		super(id, flag);
	}

	ChequingAccount(String username){
		super(username);
	}
}

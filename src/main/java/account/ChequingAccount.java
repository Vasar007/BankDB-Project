package account;


class ChequingAccount extends AccountImpl {
	ChequingAccount(String ID, boolean flag){
		super(ID, flag);
	}

	ChequingAccount(String username){
		super(username);
	}
}

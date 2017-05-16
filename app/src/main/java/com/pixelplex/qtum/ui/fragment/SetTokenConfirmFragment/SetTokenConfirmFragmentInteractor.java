package com.pixelplex.qtum.ui.fragment.SetTokenConfirmFragment;


interface SetTokenConfirmFragmentInteractor {
    void generateTokenBytecode(SetTokenConfirmFragmentInteractorImpl.GenerateTokenBytecodeCallBack callBack);
    void clearToken();
}

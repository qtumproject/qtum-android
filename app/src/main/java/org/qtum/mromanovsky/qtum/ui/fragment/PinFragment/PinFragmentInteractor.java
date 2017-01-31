package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import android.content.Context;

public interface PinFragmentInteractor {
    int getPassword();
    void savePassword(int password);
    void getWalletFromFile(PinFragmentInteractorImpl.GetWalletFromFileCallBack callBack);
    void generateRegisterKeyAndID(Context context, PinFragmentInteractorImpl.GenerateRegisterKeyAndIdentifierCallBack callBack);
}

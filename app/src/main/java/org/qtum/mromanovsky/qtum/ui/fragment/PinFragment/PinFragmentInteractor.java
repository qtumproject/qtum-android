package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


public interface PinFragmentInteractor {
    int getPassword();
    void savePassword(int password);
    void generateRegisterKeyAndID(PinFragmentInteractorImpl.generateRegisterKeyAndIdentifierCallBack callBack);
}

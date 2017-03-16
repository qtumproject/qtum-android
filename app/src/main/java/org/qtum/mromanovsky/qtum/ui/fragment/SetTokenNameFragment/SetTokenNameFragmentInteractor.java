package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenNameFragment;


interface SetTokenNameFragmentInteractor {
    void setTokenName(String name);
    void setTokenSymbol(String symbol);
    String getTokenName();
    String getTokenSymbol();
}

package com.unip.CC8P33.PontosDeColeta.ui.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminPagerAdapter extends FragmentStateAdapter {

    public AdminPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new PontosPendentesFragment();
        } else {
            return new PontosValidadosFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

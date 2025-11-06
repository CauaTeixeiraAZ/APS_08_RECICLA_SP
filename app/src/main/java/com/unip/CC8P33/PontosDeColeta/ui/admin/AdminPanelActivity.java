package com.unip.CC8P33.PontosDeColeta.ui.admin;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.unip.CC8P33.PontosDeColeta.R;

public class AdminPanelActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        toolbar.setNavigationOnClickListener(v -> finish());

        // Configurar ViewPager com Adapter
        AdminPagerAdapter adapter = new AdminPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Conectar TabLayout com ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Pendentes");
                            break;
                        case 1:
                            tab.setText("Validados");
                            break;
                    }
                }).attach();
    }
}

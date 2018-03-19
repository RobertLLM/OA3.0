package cn.invonate.ygoa3.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.carbs.android.segmentcontrolview.library.SegmentControlView;
import cn.invonate.ygoa3.Contacts.Fragment.CompanyFragment;
import cn.invonate.ygoa3.Contacts.Fragment.CyContactsFragment;
import cn.invonate.ygoa3.Contacts.SelectLocalContactsActivity;
import cn.invonate.ygoa3.R;


/**
 * Created by liyangyang on 2017/10/22.
 */

public class ContactsFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.tab_contacts)
    SegmentControlView tabContacts;
    @BindView(R.id.import_in)
    TextView importIn;

    Fragment[] fragments = new Fragment[2];

    private int currentItem = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_connect, container, false);
        unbinder = ButterKnife.bind(this, view);
        initFragment();
        tabContacts.setTexts(new String[]{"部门通讯录", "常用联系人"});
        tabContacts.setOnSegmentChangedListener(new SegmentControlView.OnSegmentChangedListener() {
            @Override
            public void onSegmentChanged(int newSelectedIndex) {
                if (currentItem != newSelectedIndex) {
                    FragmentTransaction trx = getActivity().getSupportFragmentManager().beginTransaction();
                    trx.hide(fragments[currentItem]);
                    if (!fragments[newSelectedIndex].isAdded()) {
                        trx.add(R.id.root, fragments[newSelectedIndex]);
                    }
                    trx.show(fragments[newSelectedIndex]).commit();
                    currentItem = newSelectedIndex;
                }
                if (newSelectedIndex == 1) {
                    importIn.setVisibility(View.VISIBLE);
                } else {
                    importIn.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    /**
     * 刷新常用联系人
     */
    public void refreshLocal() {
        if (fragments != null && fragments[1] != null)
            ((CyContactsFragment) fragments[1]).getCyContacts();
    }

    /**
     *
     */
    private void initFragment() {
        Fragment f1 = new CompanyFragment();
        fragments[0] = f1;
        Fragment f2 = new CyContactsFragment();
        fragments[1] = f2;

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.root, fragments[0]).add(R.id.root, fragments[1]).commit();

        importIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 1);
                } else {
                    startActivityForResult(new Intent(getActivity(), SelectLocalContactsActivity.class), 0);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

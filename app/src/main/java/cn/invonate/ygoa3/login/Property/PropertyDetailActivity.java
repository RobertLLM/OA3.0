package cn.invonate.ygoa3.login.Property;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.invonate.ygoa3.BaseActivity;
import cn.invonate.ygoa3.Entry.Property;
import cn.invonate.ygoa3.R;

public class PropertyDetailActivity extends BaseActivity {

    @BindView(R.id.index)
    TextView index;
    @BindView(R.id.asset_category_name)
    TextView assetCategoryName;
    @BindView(R.id.department_name)
    TextView departmentName;
    @BindView(R.id.finance_category_name)
    TextView financeCategoryName;
    @BindView(R.id.name_)
    TextView name;
    @BindView(R.id.principal_code)
    TextView principalCode;
    @BindView(R.id.principal_name)
    TextView principalName;
    @BindView(R.id.principal_tel)
    TextView principalTel;
    @BindView(R.id.specification_)
    TextView specification;
    @BindView(R.id.quondam_cost)
    TextView quondamCost;
    @BindView(R.id.qty)
    TextView qty;
    @BindView(R.id.measurement_)
    TextView measurement;

    private int position;//序号

    private Property.PropertyBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);
        ButterKnife.bind(this);
        data = (Property.PropertyBean) getIntent().getExtras().getSerializable("property");
        position = getIntent().getExtras().getInt("position");

        index.setText(position + "");
        assetCategoryName.setText(data.getAsset_category_name());
        departmentName.setText(data.getDepartment_name());
        financeCategoryName.setText(data.getFinance_category_name());
        name.setText(data.getName_());
        principalCode.setText(data.getPrincipal_code());
        principalName.setText(data.getPrincipal_name());
        principalTel.setText(data.getPrincipal_tel());
        specification.setText(data.getSpecification_());
        quondamCost.setText(data.getQuondam_cost());
        qty.setText(data.getQty());
        measurement.setText(data.getMeasurement_());
    }

    @OnClick(R.id.pic_back)
    public void onViewClicked() {
        finish();
    }
}

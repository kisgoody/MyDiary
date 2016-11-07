package zhao.edifier.com.mynotepaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MessageActivity extends AppCompatActivity {

    private EditText et_phone_number,et_message_number;
    private TextView tv_getMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        et_phone_number = $(R.id.et_phone_number);
        et_message_number = $(R.id.et_message_number);
        tv_getMessage = $(R.id.tv_getMessage);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            startActivity(new Intent(MessageActivity.this, MainActivity.class));
            this.finish();
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
        }
        tv_getMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = et_phone_number.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    toast("请输入手机号码");
                    return;
                }
                if (number.length() != 11) {
                    toast("手机号码格式有误");
                    return;
                }
                requestSmsCode(number);
            }
        });

        et_message_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = et_phone_number.getText().toString();
                String code = s.toString();
                if(TextUtils.isEmpty(number)){
                    return ;
                }
                if(TextUtils.isEmpty(number)){
                    return ;
                }
                if(code.isEmpty()){
                    toast("请输入验证码");
                    return;
                }
                if(code.length()==6) verifySmsCode(number,code);
            }
        });
    }
    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }

    /** 请求短信验证码
     * @method requestSmsCode
     * @return void
     * @exception
     */
    private void requestSmsCode(String number){
            BmobSMS.requestSMSCode(number, "短信验证", new QueryListener<Integer>() {

                @Override
                public void done(Integer smsId, BmobException ex) {
                    if (ex == null) {//验证码发送成功
                        toast("验证码发送成功，短信id：" + smsId);//用于查询本次短信发送详情
                    } else {
                        toast("errorCode = " + ex.getErrorCode() + ",errorMsg = " + ex.getLocalizedMessage());
                    }
                }
            });
    }

    /** 验证短信验证码
     * @method requestSmsCode
     * @return void
     * @exception
     */
    private void verifySmsCode(final String number,String code){
            BmobSMS.verifySmsCode(number, code, new UpdateListener() {

                @Override
                public void done(BmobException ex) {
                    if (ex == null) {//短信验证码已验证成功

                        BmobUser bu2 = new BmobUser();
                        bu2.setUsername(number);
                        bu2.setPassword(number);

                        bu2.login(new SaveListener<BmobUser>() {

                            @Override
                            public void done(BmobUser bmobUser, BmobException e) {
                                if (e == null) {
                                    startActivity(new Intent(MessageActivity.this, MainActivity.class));
//                                    toast("登录成功:");
                                } else {
                                    BmobUser user = new BmobUser();
                                    user.setUsername(number);
                                    user.setPassword(number);
                                    user.signUp(new SaveListener<BmobUser>() {
                                        @Override
                                        public void done(BmobUser s, BmobException e) {
                                            if (e == null) {
                                                login(s);
                                            } else {
                                                toast("注册失败:" + s.toString());
                                            }
                                        }
                                    });
//                                    loge(e);
                                }
                            }
                        });



//                        toast("验证通过");
                    } else {
                        toast("验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                    }
                }
            });
    }

    public void login(BmobUser user){
        BmobUser.loginByAccount(user.getUsername(), user.getUsername(), new LogInListener<BmobUser>() {

            @Override
            public void done(BmobUser user, BmobException e) {
                if(user!=null){
                    startActivity(new Intent(MessageActivity.this, MainActivity.class));

                }
            }
        });
    }

}

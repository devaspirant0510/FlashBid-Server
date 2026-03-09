package com.choing.flashbid.infrastructure.mail;


import org.springframework.stereotype.Component;

@Component
public class EmailTemplate {
    public String getVerificationEmailTemplate(String code) {
        return """
                <html>
                  <body style="font-family: 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #fff7f3; margin: 0; padding: 0;">
                    <table align="center" width="100%%" cellpadding="0" cellspacing="0" 
                           style="max-width: 600px; background-color: #ffffff; margin: 40px auto; 
                                  border-radius: 12px; box-shadow: 0 3px 10px rgba(0,0,0,0.08);">
                      
                      <!-- Header -->
                      <tr>
                        <td style="background: linear-gradient(90deg, #ed6c37, #f7a17e); 
                                   color: #ffffff; text-align: center; padding: 24px 0; 
                                   border-top-left-radius: 12px; border-top-right-radius: 12px;">
                          <h1 style="margin: 0; font-size: 26px; letter-spacing: 1px;">UnknownAuction</h1>
                        </td>
                      </tr>
                      
                      <!-- Body -->
                      <tr>
                        <td style="padding: 40px 30px; text-align: center;">
                          <h2 style="color: #ed6c37; margin-bottom: 12px;">이메일 인증 코드</h2>
                          <p style="color: #444444; font-size: 15px; line-height: 1.6;">
                            안녕하세요 👋<br>
                            <b>UnknownAuction</b> 서비스를 이용해주셔서 감사합니다.<br>
                            아래 인증 코드를 입력해 이메일을 인증해주세요.
                          </p>

                          <div style="background-color: #ed6c37; color: #ffffff; display: inline-block; 
                                      font-size: 28px; font-weight: bold; letter-spacing: 4px; 
                                      padding: 14px 36px; border-radius: 8px; margin: 24px 0;">
                            %s
                          </div>

                          <p style="color: #777777; font-size: 13px; margin-top: 10px;">
                            ⏰ 이 코드는 <strong>10분간</strong>만 유효합니다.
                          </p>

                          <hr style="border: none; border-top: 1px solid #f2f2f2; margin: 30px 0;">
                          <p style="color: #999999; font-size: 12px;">
                            본 메일은 발신 전용입니다.<br>
                            문의사항이 있다면 UnknownAuction 고객센터를 이용해주세요.
                          </p>
                        </td>
                      </tr>

                      <!-- Footer -->
                      <tr>
                        <td style="background-color: #f7a17e; color: #ffffff; font-size: 12px; 
                                   text-align: center; padding: 14px; 
                                   border-bottom-left-radius: 12px; border-bottom-right-radius: 12px;">
                          © 2025 UnknownAuction. All rights reserved.
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """.formatted(code);
    }
}

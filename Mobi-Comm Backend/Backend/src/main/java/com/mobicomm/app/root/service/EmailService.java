package com.mobicomm.app.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPaymentConfirmation(String toEmail, String userName, String planName, 
                                        String amount, String expiryDate, String paymentId) {
        try {
            // ✅ Format expiry date (yyyy-MM-dd)
            String formattedExpiryDate = expiryDate.split("T")[0]; 

            // ✅ Create a MimeMessage for HTML email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("📩 Payment Confirmation - MobiComm ✅");
            
            // ✅ Professional HTML Email Template
            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 500px; border: 1px solid #ddd; border-radius: 10px;'>"
                    + "<h2 style='color: #007bff; text-align: center;'>MobiComm Payment Confirmation</h2>"
                    + "<p style='font-size: 16px;'>Dear <strong>" + userName + "</strong>,</p>"
                    + "<p>Your payment has been successfully processed. Below are the details:</p>"
                    + "<table style='width: 100%; border-collapse: collapse; font-size: 16px;'>"
                    + "  <tr><td style='padding: 8px;'><strong>📌 Payment ID:</strong></td><td style='padding: 8px;'>" + paymentId + "</td></tr>"
                    + "  <tr><td style='padding: 8px;'><strong>📦 Plan:</strong></td><td style='padding: 8px;'>" + planName + "</td></tr>"
                    + "  <tr><td style='padding: 8px;'><strong>💰 Amount Paid:</strong></td><td style='padding: 8px;'>₹" + amount + "</td></tr>"
                    + "  <tr><td style='padding: 8px;'><strong>📅 Expiry Date:</strong></td><td style='padding: 8px;'>" + formattedExpiryDate + "</td></tr>"
                    + "</table>"
                    + "<p style='font-size: 16px;'>Thank you for choosing <strong>MobiComm</strong>! Stay connected. 🚀</p>"
                    + "<p style='font-size: 14px; color: #666;'>For support, contact <a href='mailto:support@mobicomm.com'>support@mobicomm.com</a></p>"
                    + "<hr style='border-top: 1px solid #ddd;'>"
                    + "<p style='text-align: center; font-size: 12px; color: #666;'>© 2025 MobiComm Pvt Ltd. All Rights Reserved.</p>"
                    + "</div>";

            helper.setText(htmlContent, true); // ✅ Send HTML content
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace(); // Log error
        }
    }
}

package seoil.capstone.flashbid.global.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class KakaoAccount {
    @JsonProperty("profile_needs_agreement")
    private Boolean profileNeedsAgreement;

    @JsonProperty("profile_nickname_needs_agreement")
    private Boolean profileNicknameNeedsAgreement;

    @JsonProperty("profile_image_needs_agreement")
    private Boolean profileImageNeedsAgreement;

    @JsonProperty("profile")
    private Profile profile;
    @JsonProperty("name_needs_agreement")
    private Boolean nameNeedsAgreement;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email_needs_agreement")
    private Boolean emailNeedsAgreement;
    @JsonProperty("is_email_valid")
    private Boolean isEmailValid;
    @JsonProperty("is_email_verified")
    private Boolean isEmailVerified;
    @JsonProperty("email")
    private String email;
    @JsonProperty("age_range_needs_agreement")
    private Boolean ageRangeNeedsAgreement;
    @JsonProperty("age_range")
    private String ageRange;
    @JsonProperty("birthyear_needs_agreement")
    private Boolean birthyearNeedsAgreement;
    @JsonProperty("birthyear")
    private String birthyear;
    @JsonProperty("birthday_needs_agreement")
    private Boolean birthdayNeedsAgreement;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("birthday_type")
    private String birthdayType;
    @JsonProperty("is_leap_month")
    private Boolean isLeapMonth;
    @JsonProperty("gender_needs_agreement")
    private Boolean genderNeedsAgreement;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("phone_number_needs_agreement")
    private Boolean phoneNumberNeedsAgreement;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("ci_needs_agreement")
    private Boolean ciNeedsAgreement;
    @JsonProperty("ci")
    private String ci;
    @JsonProperty("ci_authenticated_at")
    private LocalDateTime ciAuthenticatedAt;

    public Boolean getProfileNeedsAgreement() {
        return profileNeedsAgreement;
    }

    public void setProfileNeedsAgreement(Boolean profileNeedsAgreement) {
        this.profileNeedsAgreement = profileNeedsAgreement;
    }

    public Boolean getProfileNicknameNeedsAgreement() {
        return profileNicknameNeedsAgreement;
    }

    public void setProfileNicknameNeedsAgreement(Boolean profileNicknameNeedsAgreement) {
        this.profileNicknameNeedsAgreement = profileNicknameNeedsAgreement;
    }

    public Boolean getProfileImageNeedsAgreement() {
        return profileImageNeedsAgreement;
    }

    public void setProfileImageNeedsAgreement(Boolean profileImageNeedsAgreement) {
        this.profileImageNeedsAgreement = profileImageNeedsAgreement;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Boolean getNameNeedsAgreement() {
        return nameNeedsAgreement;
    }

    public void setNameNeedsAgreement(Boolean nameNeedsAgreement) {
        this.nameNeedsAgreement = nameNeedsAgreement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEmailNeedsAgreement() {
        return emailNeedsAgreement;
    }

    public void setEmailNeedsAgreement(Boolean emailNeedsAgreement) {
        this.emailNeedsAgreement = emailNeedsAgreement;
    }

    public Boolean getEmailValid() {
        return isEmailValid;
    }

    public void setEmailValid(Boolean emailValid) {
        isEmailValid = emailValid;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAgeRangeNeedsAgreement() {
        return ageRangeNeedsAgreement;
    }

    public void setAgeRangeNeedsAgreement(Boolean ageRangeNeedsAgreement) {
        this.ageRangeNeedsAgreement = ageRangeNeedsAgreement;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public Boolean getBirthyearNeedsAgreement() {
        return birthyearNeedsAgreement;
    }

    public void setBirthyearNeedsAgreement(Boolean birthyearNeedsAgreement) {
        this.birthyearNeedsAgreement = birthyearNeedsAgreement;
    }

    public String getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(String birthyear) {
        this.birthyear = birthyear;
    }

    public Boolean getBirthdayNeedsAgreement() {
        return birthdayNeedsAgreement;
    }

    public void setBirthdayNeedsAgreement(Boolean birthdayNeedsAgreement) {
        this.birthdayNeedsAgreement = birthdayNeedsAgreement;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthdayType() {
        return birthdayType;
    }

    public void setBirthdayType(String birthdayType) {
        this.birthdayType = birthdayType;
    }

    public Boolean getLeapMonth() {
        return isLeapMonth;
    }

    public void setLeapMonth(Boolean leapMonth) {
        isLeapMonth = leapMonth;
    }

    public Boolean getGenderNeedsAgreement() {
        return genderNeedsAgreement;
    }

    public void setGenderNeedsAgreement(Boolean genderNeedsAgreement) {
        this.genderNeedsAgreement = genderNeedsAgreement;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getPhoneNumberNeedsAgreement() {
        return phoneNumberNeedsAgreement;
    }

    public void setPhoneNumberNeedsAgreement(Boolean phoneNumberNeedsAgreement) {
        this.phoneNumberNeedsAgreement = phoneNumberNeedsAgreement;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getCiNeedsAgreement() {
        return ciNeedsAgreement;
    }

    public void setCiNeedsAgreement(Boolean ciNeedsAgreement) {
        this.ciNeedsAgreement = ciNeedsAgreement;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public LocalDateTime getCiAuthenticatedAt() {
        return ciAuthenticatedAt;
    }

    public void setCiAuthenticatedAt(LocalDateTime ciAuthenticatedAt) {
        this.ciAuthenticatedAt = ciAuthenticatedAt;
    }

    // Getters & Setters
}

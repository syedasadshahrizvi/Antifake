package com.antifake.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Company {
	
    private Integer companyId;
    
    private String userId;

    private String companyName;

	private String registerId;

    private Byte level;

    private Byte status;

    private String businessLicense;
    
    private String companyCode;
    

    private String createTime;
    
    

    public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	 public String getCreateTime() {
			return createTime;
		}

	
		public void setCreateTime(Timestamp date) {
			
			Date dates = new Date(date.getTime());
			
			
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm").format(dates);
			this.createTime=timeStamp;
			//System.out.println("string"+ timeStamp);
			//this.createDate.setTime( date.getTime());
			
		}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId == null ? null : registerId.trim();
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense == null ? null : businessLicense.trim();
    }

	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", userId=" + userId + ", companyName=" + companyName
				+ ", registerId=" + registerId + ", level=" + level + ", status=" + status + ", businessLicense="
				+ businessLicense + ", companyCode=" + companyCode + "]";
	}

}
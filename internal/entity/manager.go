package entity

// ManagerUser 管理端登录账号
type ManagerUser struct {
	ID           int64   `gorm:"primaryKey;autoIncrement"`
	LoginName    string  `gorm:"column:login_name;type:varchar(64);uniqueIndex;not null"`
	PasswordHash string  `gorm:"column:password_hash;type:varchar(128);not null"`
	OtpSecret    *string `gorm:"column:otp_secret;type:varchar(64)"`
	OtpEnabled   int16   `gorm:"column:otp_enabled;not null;default:0"`
	RealName     *string `gorm:"column:real_name;type:varchar(32)"`
	State        int16   `gorm:"column:state;not null;default:1"`
	LastLoginAt  *int64  `gorm:"column:last_login_at"` // Unix 毫秒
	CreatedAt    int64   `gorm:"column:created_at;not null"` // Unix 毫秒
	UpdatedAt    int64   `gorm:"column:updated_at;not null"` // Unix 毫秒
}

func (ManagerUser) TableName() string { return "manager.manager_user" }

package service

import (
	"errors"
	"os"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"golang.org/x/crypto/bcrypt"
)

const bcryptCost = 10
const defaultJWTExpire = 24 * time.Hour

var (
	ErrWrongPassword = errors.New("密码错误")
	ErrUserDisabled  = errors.New("账号已禁用")
)

func HashPassword(password string) (string, error) {
	b, err := bcrypt.GenerateFromPassword([]byte(password), bcryptCost)
	return string(b), err
}

func CheckPassword(hash, password string) bool {
	err := bcrypt.CompareHashAndPassword([]byte(hash), []byte(password))
	return err == nil
}

func JWTSecret() []byte {
	s := os.Getenv("JWT_SECRET")
	if s == "" {
		s = "easy_pay_jwt_secret_change_in_production"
	}
	return []byte(s)
}

type AdminClaims struct {
	jwt.RegisteredClaims
	UserID   uint   `json:"uid"`
	Username string `json:"un"`
	Type     string `json:"type"`
}

type AgentClaims struct {
	jwt.RegisteredClaims
	UserID   uint   `json:"uid"`
	Username string `json:"un"`
	AgentID  uint   `json:"aid"`
	Type     string `json:"type"`
}

func IssueAdminToken(userID uint, username string) (string, time.Time, error) {
	exp := time.Now().Add(defaultJWTExpire)
	claims := AdminClaims{
		RegisteredClaims: jwt.RegisteredClaims{ExpiresAt: jwt.NewNumericDate(exp)},
		UserID:           userID,
		Username:         username,
		Type:             "admin",
	}
	t := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	s, err := t.SignedString(JWTSecret())
	return s, exp, err
}

func IssueAgentToken(userID uint, username string, agentID uint) (string, time.Time, error) {
	exp := time.Now().Add(defaultJWTExpire)
	claims := AgentClaims{
		RegisteredClaims: jwt.RegisteredClaims{ExpiresAt: jwt.NewNumericDate(exp)},
		UserID:           userID,
		Username:         username,
		AgentID:          agentID,
		Type:             "agent",
	}
	t := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	s, err := t.SignedString(JWTSecret())
	return s, exp, err
}

func ParseAdminToken(tokenString string) (*AdminClaims, error) {
	t, err := jwt.ParseWithClaims(tokenString, &AdminClaims{}, func(*jwt.Token) (interface{}, error) {
		return JWTSecret(), nil
	})
	if err != nil {
		return nil, err
	}
	if c, ok := t.Claims.(*AdminClaims); ok && t.Valid && c.Type == "admin" {
		return c, nil
	}
	return nil, errors.New("invalid token")
}

func ParseAgentToken(tokenString string) (*AgentClaims, error) {
	t, err := jwt.ParseWithClaims(tokenString, &AgentClaims{}, func(*jwt.Token) (interface{}, error) {
		return JWTSecret(), nil
	})
	if err != nil {
		return nil, err
	}
	if c, ok := t.Claims.(*AgentClaims); ok && t.Valid && c.Type == "agent" {
		return c, nil
	}
	return nil, errors.New("invalid token")
}

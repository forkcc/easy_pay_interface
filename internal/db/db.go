package db

import (
	"sync"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

var (
	once sync.Once
	_db  *gorm.DB
)

// Init 使用 DSN 初始化全局 DB（仅首次有效）
func Init(dsn string) error {
	var err error
	once.Do(func() {
		_db, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})
	})
	return err
}

// DB 返回全局 *gorm.DB，未初始化时返回 nil
func DB() *gorm.DB {
	return _db
}

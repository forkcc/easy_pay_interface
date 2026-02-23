package main

import (
	"log"
	"os"

	"github.com/easypay/easy_pay_interface/internal/app"
	"github.com/gin-gonic/gin"
)

func main() {
	addr := os.Getenv("HTTP_ADDR")
	if addr == "" {
		addr = ":8080"
	}

	db, err := app.OpenDB()
	if err != nil {
		log.Fatal("db: ", err)
	}

	router := gin.Default()
	app.SetupRoutes(router, db)
	log.Printf("listen %s", addr)
	if err := router.Run(addr); err != nil {
		log.Fatal(err)
	}
}

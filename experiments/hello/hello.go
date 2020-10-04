package main

import (
    "fmt"

    "plutus-experiments/greetings"
)

func main() {
    // Get a greeting message and print it.
    message := greetings.Hello("Thom")
    fmt.Println(message)
}

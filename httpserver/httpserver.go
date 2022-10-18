package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
)

func hello(w http.ResponseWriter, req *http.Request) {

	fmt.Fprintf(w, "hello\n")
}

func headers(w http.ResponseWriter, req *http.Request) {

	for name, headers := range req.Header {
		for _, h := range headers {
			fmt.Fprintf(w, "%v: %v\n", name, h)
		}
	}
}

// receiveHandler accepts the file and saves it to the current working directory
func receiveHandler(w http.ResponseWriter, r *http.Request) {
	fmt.Printf("In the file upload methd")
	switch r.Method {

	case "POST":
		// the FormFile function takes in the POST input id file
		fmt.Println(r)
		file, header, err := r.FormFile("file")

		if err != nil {
			fmt.Fprintln(w, err)
			return
		}

		defer file.Close()

		out, err := os.Create(header.Filename)
		if err != nil {
			fmt.Fprintf(w, "Unable to create the file for writing. Check your write access privilege")
			return
		}

		defer out.Close()

		// write the content from POST to the file
		_, err = io.Copy(out, file)
		if err != nil {
			fmt.Fprintln(w, err)
		}

		fmt.Fprintf(w, "File uploaded successfully: ")
		fmt.Fprintf(w, header.Filename)
	}

}
func main() {

	http.HandleFunc("/hello", hello)
	http.HandleFunc("/headers", headers)
	http.HandleFunc("/receive", receiveHandler)

	http.ListenAndServe(":8080", nil)
}

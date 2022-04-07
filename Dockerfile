FROM java:8
MAINTAINER wk@email.com
ADD /target/hani-0.1.jar hani.jar
ENTRYPOINT ["java", "-jar", "hani.jar"]
CMD ["java", "-version"]

docker build --rm -t fillpdf .
&& docker run --name fillPDF -p 8080:8080 fillpdf


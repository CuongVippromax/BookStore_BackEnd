# Tải maven bản 3.9.8
# ----------- STAGE 1: Build -----------
# Dùng Maven 3.9.8 với JDK 17
FROM maven:3.9.8-amazoncorretto-17 AS build

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy file pom.xml để cache dependencies
COPY pom.xml .

# Copy toàn bộ mã nguồn
COPY src ./src

# Build ứng dụng (bỏ qua test)
RUN mvn clean package -DskipTests


# ----------- STAGE 2: Runtime -----------
# Dùng Amazon Corretto 17 (JDK 17)
FROM amazoncorretto:17.0.13

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy file jar từ stage build
COPY --from=build /app/target/*.jar app.jar

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]

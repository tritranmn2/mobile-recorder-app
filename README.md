# mobile-recorder-app

Author: Made by Group09
#tritranmn2
Cách làm việc với git
0. B0: Clone code về local trước khi làm việc(nếu chưa clone). Câu lệnh git clone https://github.com/tritranmn2/mobile-recorder-app.git
1. B1: Pull code từ branch dev về local trước khi làm 1 chức năng. Câu lệnh git pull origin dev
2. B2 Tạo nhánh mới (tên nhánh có thể tự đặt hoặc lấy bên jira). Câu lệnh git checkout -b <tên nhánh>
    VD: git checkout -b feature/record
3. B3: Cuối buổi làm việc commit code(commit lên nhánh mình đang làm). Câu lệnh git commmit -m "tao chuc nang moi 1"
4. B4: Push code lên githug. Câu lênh git push origin feature/record (lưu ý feature/record là tên nhánh tạo ở local ở B2).
Nếu push mà xung đột code thì phải pull rồi xử lí rồi push lại. Câu lệnh như bước 5
5. B5: Pull code để merge code vào branch dev. Câu lệnh git pull origin dev 
LƯU Ý: 
        Không push code vào nhánh dev hoặc main.

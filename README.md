# TraderPro
TraderPro
1.	Giới thiệu Trader Analytics
Trader Analytics là 1 hệ thống ứng dụng bao gồm 2 thành phần 
-	Server chuyên thu thập xử lý các thông tin từ 2 sàn giao dịch Bittrex và Binance
-	App mobile nhận các thông tin từ Server, thông báo tới người sử dụng cuối Coin nào nêm mua vào và bán ra để được lợi nhuận cao nhất.

Cơ chế hoạt động
-	Server liên tục quét các biến động trên sàn
-	Tính toán các thông số dựa trên các thông tin thu thập được
•	Số lượng buy/sell
•	Số lượng taker/maker
•	Sự biến động giá trong một thời điểm
•	Sự biến động volume trong một thời điểm 
•	Kết hợp với các chỉ báo cơ bản BB, RSI, …
=>	Truy tìm và đưa ra gợi ý đồng Coin nào đang có khả năng tăng giá, giảm giá, mua vào bán ra tại thời điểm nào là hợp lý.  

Vì sao nên lựa chọn Trader Analytics
-	Server chạy 24/24, quét gần 150 coin có mặt trên Bittrex và gần 120 coin có mặt trên Binance
-	Đưa ra các thông số chi tiết để lý giải tại sao đồng Coin nào đó có thể tăng giá
-	Các bản nâng cấp sẽ còn bao gồm các tính năng
•	Trade trên sàn thông qua app: Bittrex, Houbi (đang phát triển)
•	Auto check & take Analyticsfit (đã phát triển)
























2.	Hướng dẫn sử dụng
2.1  Thông báo mua
•	Màn hình Notification thông báo mua
 

Chú giải
Vùng 1. Gồm tên coin được báo. Tức là hệ thống đã quét được 1 coin nghi vấn, dựa trên các thông số tính toán được, đưa ra rằng coin này có khả năng sẽ tăng trong thời gian ngắn.
Thời gian thích hợp để trade đồng coin mà hệ thống đưa ra là khoảng trong vòng 1 phút đến 6h

Vùng 2. Các thông số chi tiết
- Giá OP: giá mở phiên (tính theo H1)
- Giá HT: giá hiện tại
- Giá 1H: giá cùng kỳ của 1 giờ trước
  Tương tự với các khoảng giá còn lại
- Vol HT: volume hiện tại để so sánh với volume của các khung giờ trước


	Dựa vào đây có thể cân nhắc xem có nên vào lệnh hay không. Nếu thấy giá hiện tại đang có xu hướng tăng + khối lượng giao dịch đang rất lớn 
	Khả năng vào lệnh ăn sẽ rất cao














•	Màn hình chính trên app									 

2.2	Thông báo bán
•	Màn hình Notification thông báo bán 
•	Màn hình chính trên app


 
		

3.	Chú ý cần thiết để trade thành công với Trader Analytics
1.	Không ALL IN mọi trường hợp
2.	Theo kinh nghiệm, khi Growth rate khoảng < 200% thì sau khi mua có thể đặt bán lời 2%
khi Growth rate khoảng > 200% thì sau khi mua có thể đặt bán lời tăng dần
3.	Gặp thị trường bão, hết sức cẩn thận khi ra vào lệnh
4.	Trader Analytics là một công cụ đưa ra cảnh báo, công cụ không thể đúng 100% vì nếu đúng 100% thì đã có thể bán nhà đi ALL IN rồi, nhưng tỉ lệ thành công của Trader Analytics ở thời điểm hiên tại là khoảng 80% -> khá cao
5.	Nên tránh nhưng con có Volume Deteced < 2, vì những con này volume nhỏ, dễ bị làm giá, đảo trend



***
CHÚC CÁC BẠN TRADE MỖI NGÀY MỘT THÀNH CÔNG HƠN VỚI TRADER ANALYTICS

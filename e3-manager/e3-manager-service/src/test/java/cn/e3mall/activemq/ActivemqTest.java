package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActivemqTest {
	
	@Test
	public void queueProduct() throws Exception{
		// 1、向工程中添加Activemq的客户端jar包。
		// 2、创建一个Connectionfactory对象，构造参数，activemq服务的ip地址和端口号。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.169.130:61616");
		// 3、使用ConnectionFactory创建一个Connection对象。
		Connection createConnection = connectionFactory.createConnection();
		// 4、开启连接，调用Connection对象的start方法。
		createConnection.start();
		// 5、使用Connection对象创建一个Session对象
		//1.参数1.是否开启事务，参数2.如果参数1为true参数2没有意义false：消息的应答模式，一种是自动应答，一种是手动应答
		Session session = createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 6、使用Session对象创建一个Destination对象，两种：queue、topic
		Queue queue = session.createQueue("test_queue");
		// 7、使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue);
		// 8、创建一个TextMessage对象
		/*TextMessage textmessage = new ActiveMQTextMessage();
		textmessage.setText("hello activemq");*/
		TextMessage textMessage = session.createTextMessage("hello activemq");
		// 9、发送消息
		producer.send(textMessage);
		// 10、关闭资源
		producer.close();
		session.close();
		createConnection.close();
	}
	
	@Test
	public void queueConsumer() throws Exception {
		// 1、向工程中添加Activemq的客户端jar包。
		// 2、创建一个Connectionfactory对象，构造参数，activemq服务的ip地址和端口号。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.169.130:61616");
		// 3、使用ConnectionFactory创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		// 4、开启连接，调用Connection对象的start方法。
		connection.start();
		// 5、使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 6、使用Session对象创建一个Destination对象，两种：queue、topic
		Queue queue = session.createQueue("test_queue");
		// 7、使用Session对象创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		// 8、设置消息的监听对象，MessageListener的实现类。
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// 9、系统等待接收消息，System.in.read()
		System.out.println("queue消费者已经启动。。。。。。");
		System.in.read();
		// 11、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void topicProducer() throws Exception {
		// 1、向工程中添加Activemq的客户端大jar包。
		// 2、创建一个Connectionfactory对象，构造参数，activemq服务的ip地址和端口号。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.169.130:61616");
		// 3、使用ConnectionFactory创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		// 4、开启连接，调用Connection对象的start方法。
		connection.start();
		// 5、使用Connection对象创建一个Session对象
		//参数1：是否开启事务，参数2：如果参数1为true参数2没有意义。为false：消息的应答模式。一种是自动应答一种是手动应答。
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 6、使用Session对象创建一个Destination对象，两种：queue、topic
		Topic topic = session.createTopic("test-topic");
		// 7、使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(topic);
		// 8、创建一个TextMessage对象
		/*TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello activemq");*/
		TextMessage textMessage = session.createTextMessage("hello activemq1111");
		// 9、发送消息
		producer.send(textMessage);
		// 10、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void topicConsumer() throws Exception {
		// 1、向工程中添加Activemq的客户端jar包。
		// 2、创建一个Connectionfactory对象，构造参数，activemq服务的ip地址和端口号。
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.169.130:61616");
		// 3、使用ConnectionFactory创建一个Connection对象。
		Connection connection = connectionFactory.createConnection();
		// 4、开启连接，调用Connection对象的start方法。
		connection.setClientID("client1");
		connection.start();
		// 5、使用Connection对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 6、使用Session对象创建一个Destination对象，两种：queue、topic
		Topic topic = session.createTopic("test-topic");
		// 7、使用Session对象创建一个Consumer对象
		/*MessageConsumer consumer = session.createConsumer(topic);*/
		TopicSubscriber consumer = session.createDurableSubscriber(topic, "meimei");
		// 8、设置消息的监听对象，MessageListener的实现类。
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				// 10、接收消息，并打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		// 9、系统等待接收消息，System.in.read()
		System.out.println("topic消费者3已经启动。。。。。。");
		System.in.read();
		// 11、关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}

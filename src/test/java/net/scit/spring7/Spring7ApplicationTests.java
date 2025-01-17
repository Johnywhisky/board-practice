package net.scit.spring7;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.scit.spring7.entity.BoardEntity;
import net.scit.spring7.repository.BoardRepository;


@SpringBootTest
class Spring7ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	BoardRepository boardRepo;

	@Test
	void testInsertBoard() {
		String[] writers = {"qweqwe", "박준범", "송지율", "오세준", "윤서준"};
		String[] contents = {
				"二人の間 通り過ぎた風は",
				"どこから寂しさを運んできたの",
				"泣いたりしたそのあとの空は",
				"やけに透き通っていたりしたんだ",
				"いつもは尖ってた父の言葉が",
				"今日は暖かく感じました",
				"優しさも笑顔も夢の語り方も",
				"知らなくて全部 君を真似たよ",
				"もう少しだけでいい あと少しだけでいい",
				"もう少しだけでいいから",
				"もう少しだけでいい あと少しだけでいい",
				"もう少しだけくっついていようか",
				"僕らタイムフライヤー 時を駆け上がるクライマー",
				"時のかくれんぼ はぐれっこはもういやなんだ",
				"嬉しくて泣くのは 悲しくて笑うのは",
				"君の心が 君を追い越したんだよ",
				"星にまで願って 手にいれたオモチャも",
				"部屋の隅っこに今 転がってる",
				"叶えたい夢も 今日で100個できたよ",
				"たった一つといつか 交換こしよう",
				"いつもは喋らないあの子に今日は",
				"放課後「また明日」と声をかけた",
				"慣れないこともたまにならいいね",
				"特にあなたが 隣にいたら",
				"もう少しだけでいい あと少しだけでいい",
				"もう少しだけでいいから",
				"もう少しだけでいい あと少しだけでいい",
				"もう少しだけくっついていようよ",
				"僕らタイムフライヤー 君を知っていたんだ",
				"僕が 僕の名前を 覚えるよりずっと前に",
				"君のいない 世界にも 何かの意味はきっとあって",
				"でも君のいない 世界など 夏休みのない 八月のよう",
				"君のいない 世界など 笑うことない サンタのよう",
				"君のいない 世界など",
				"僕らタイムフライヤー 時を駆け上がるクライマー",
				"時のかくれんぼ はぐれっこはもういやなんだ",
				"なんでもないや やっぱりなんでもないや",
				"今から行くよ",
				"僕らタイムフライヤー 時を駆け上がるクライマー",
				"時のかくれんぼ はぐれっこ はもういいよ",
				"君は派手なクライヤー その涙 止めてみたいな",
				"だけど 君は拒んだ 零れるままの涙を見てわかった",
				"嬉しくて泣くのは 悲しくて 笑うのは",
				"僕の心が 僕を追い越したんだよ"
		};
		
		for (int i = 0; i < 250; i++) {
			int idxWriters = (int) (Math.random() * writers.length);
			int idxContents = (int) (Math.random() * contents.length);
			String writer = writers[idxWriters];
			String content = contents[idxContents];
			String title = "제목: " + content.substring(0, 3) + "...";

			BoardEntity entity = new BoardEntity();
			entity.setWriter(writer);
			entity.setContent(content);
			entity.setTitle(title);

			boardRepo.save(entity);
		}
	}
}

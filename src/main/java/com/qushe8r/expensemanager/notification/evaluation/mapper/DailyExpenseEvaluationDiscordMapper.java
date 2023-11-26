package com.qushe8r.expensemanager.notification.evaluation.mapper;

import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationInformation;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordBody;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbed;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbedAuthor;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbedField;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbedFooter;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbedThumbnail;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DailyExpenseEvaluationDiscordMapper {

  private String username = "Expense-Manager";

  private String avatarUrl =
      "https://github-production-user-asset-6210df.s3.amazonaws.com/115606959/283298026-1357cebc-5d3e-4264-b9cb-f17ad1fdc442.png";

  private String content = "### 오늘도 고생하셨습니다! 하루를 마무리 해봅시다!";

  private String authorIconUrl = "https://cdn-icons-png.flaticon.com/256/5726/5726532.png";

  private String thumbnailUrl =
      "https://github-production-user-asset-6210df.s3.amazonaws.com/115606959/283298096-8367f187-09af-442e-a5e4-eb669338a7fa.png";

  private Integer color = 15258703;

  private String text = "이용해 주셔서 감사합니다.";

  public DiscordBody toDiscordBody(List<DailyExpenseEvaluationInformation> informations) {
    String todayFormat = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    String name = todayFormat + "\n오늘의 지출 내역은?";

    DiscordEmbedAuthor author = new DiscordEmbedAuthor(name, authorIconUrl);
    DiscordEmbedThumbnail thumbnail = new DiscordEmbedThumbnail(this.thumbnailUrl);
    DiscordEmbedFooter footer = new DiscordEmbedFooter(text);

    Calendar today = Calendar.getInstance();
    int date = today.get(Calendar.DATE);

    List<DiscordEmbedField> fields =
        informations.stream()
            .map(
                information -> {
                  Long budgetAmount = information.budgetAmount();
                  Long previousDayTotalExpenses = information.previousDayTotalExpenses();
                  Long todayTotalExpenses = information.todayTotalExpenses();

                  String assessmentPhrase =
                      assessmentPhrase(previousDayTotalExpenses, todayTotalExpenses);

                  Long appropriateDailyExpense =
                      ((budgetAmount - previousDayTotalExpenses) / date / 1000L) * 1000;

                  StringBuilder sb = new StringBuilder();
                  sb.append(assessmentPhrase)
                      .append("\n금일 적정 사용 금액: ")
                      .append(appropriateDailyExpense)
                      .append("\n금일 사용 금액: ")
                      .append(todayTotalExpenses);

                  return new DiscordEmbedField(information.categoryName(), sb.toString());
                })
            .toList();

    DiscordEmbed discordEmbed = new DiscordEmbed(author, thumbnail, color, fields, footer);

    return new DiscordBody(username, avatarUrl, content, List.of(discordEmbed));
  }

  private String assessmentPhrase(Long previousDayTotalExpenses, Long todayTotalExpenses) {
    if (previousDayTotalExpenses >= todayTotalExpenses) {
      return "잘하고 있습니다.";
    }
    return "지출이 계획보다 많습니다.";
  }
}
